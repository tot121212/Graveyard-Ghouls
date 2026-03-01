package com.totsnuk.graveyardghouls.pojo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.totsnuk.graveyardghouls.dto.GameActionDto;
import com.totsnuk.graveyardghouls.enums.GameActionEnum;
import com.totsnuk.graveyardghouls.state.GameLifecycleState;
import com.totsnuk.graveyardghouls.state.StateMachine;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
public class Game {
    /***
     * - Single action queue processed on one game thread; each player may have only
     * one pending
     * action, and interrupts are only accepted during explicit windows with the
     * game logically
     * paused while resolving them. - Structural validation occurs before enqueue,
     * full state
     * validation on execution, ensuring determinism and resilience to spam. ---
     * Animations - Player
     * animations should play concurrently on the client-side and each action should
     * be declarative
     * server-side - No matter what happens the sequence of actions on each client
     * is the same
     */
    private final int MAX_ACTION_QUEUE_SIZE = 2;
    private final Queue<GameAction> actionQueue = new ConcurrentLinkedQueue<>();
    private final int maxInterruptsPerPlayer = 1;

    private final List<Player> players = new ArrayList<>();
    private Player currentPlayer;

    /*** State machine that represents the lobby/lifecycle state */
    private final StateMachine<GameLifecycleState> lifecycleStateMachine = new StateMachine<>(GameLifecycleState.LOBBY);

    private final SeatRegistry seatRegistry = new SeatRegistry();

    private final Map<GameActionEnum, GameActionDescriptor> elementToDescriptor = new ConcurrentHashMap<>();

    public synchronized void init() {
    }

    public synchronized void reset() {
        this.actionQueue.clear();
        this.players.clear();
        this.lifecycleStateMachine.set(null, null);
        // shouldn't do this because we want to return to lobby after game ends
        // this.seatRegistry.reset();
    }

    public boolean isPlayerTurn(Player player) {
        return player == currentPlayer;
    }

    /**
     * Validates action via metadata,current state
     * creates from dto
     * 
     * @param action
     * @return
     */
    public synchronized GameAction validateAndCreate(GameActionDto dto) {
        if (dto == null)
            return null;

        Enum<?> e = dto.getElement();
        if (e == null || !(e instanceof GameActionEnum))
            return null;
        GameActionEnum actionEnum = (GameActionEnum) e;
        Player player = dto.getPlayer();
        Record payload = dto.getPayload();

        if (player == null || payload == null)
            return null;

        if (!players.contains(player))
            return null;

        // find descriptor from map
        GameActionDescriptor descriptor = elementToDescriptor.get(actionEnum);
        return new GameActionImpl(actionEnum, player, payload, descriptor);
    }

    /**
     * - Adds a game action to the queue
     * - This should perform all necessary validation before adding to the queue
     */
    public synchronized boolean enqueue(GameActionDto dto) {
        if (dto == null)
            return false;

        GameAction action = validateAndCreate(dto);
        if (action == null)
            return false;

        Enum<?> actionEnum = action.getElement();
        Player player = action.getPlayer();
        Record payload = action.getPayload();

        if (player == null || payload == null || actionEnum == null)
            return false;

        if (isPlayerTurn(player)) {
            // enqueue action normally
            actionQueue.add(action);
            return true;
        } else if (action.getDescriptor().isRealtime()) {
            // enqueue as an interrupt (removing all other actions)
            actionQueue.clear();
            actionQueue.add(action);
            return true;
        }
        return false;
    }

    /**
     * Retrieves and removes the next normal game action, or null if empty.
     */
    public GameAction pollAction() {
        return actionQueue.poll();
    }

    public void onPlayerLeave(Player player) {
        // TODO:
    }
}
