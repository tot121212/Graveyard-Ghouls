package com.totsnuk.graveyardghouls.pojo;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.totsnuk.graveyardghouls.dto.PlayerActionDto;
import com.totsnuk.graveyardghouls.events.EventDispatcher;
import com.totsnuk.graveyardghouls.events.PlayerAction;
import com.totsnuk.graveyardghouls.events.PlayerActionImpl;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
public class Game {
    private final EventDispatcher eventBus = new EventDispatcher();
    private final GameState gameState = new GameState(eventBus);

    private final GameLifecycleHandler lifecycleHandler = new GameLifecycleHandler(gameState, eventBus);
    private final GameActionSequencer gameActionSequencer = new GameActionSequencer(gameState);

    private final SeatRegistry seatRegistry = new SeatRegistry();

    private final Queue<GameUpdate> updateQueue = new ConcurrentLinkedQueue<>();

    private final List<Player> players = new ArrayList<>();

    private Player currentPlayer;

    public void reset() {
        this.gameActionSequencer.clear();
        this.players.clear();
        // shouldn't do this -V- because we want to return to lobby after game ends
        // this.seatRegistry.reset();
    }

    public boolean isPlayerTurn(Player player) {
        return player == currentPlayer;
    }

    private boolean isValidPlayerAction(PlayerAction action) {
        if (action == null || !players.contains(action.getPlayer()))
            return false;

        return action.getDescriptor().isRealtime() || isPlayerTurn(action.getPlayer());
    }

    /**
     * - Adds a game action to the queue
     * - This should perform all necessary validation before adding to the queue
     * - Also interacts with the RealtimeStack
     * ---
     * This style of enqueue ensures that currentPlayer can play actions
     * sequentially without waiting for animations to finish
     */
    public synchronized boolean enqueue(PlayerActionDto dto) {
        if (dto == null)
            return false;

        PlayerAction pAction = PlayerActionImpl.from(dto);
        if (pAction == null)
            return false;

        if (!isValidPlayerAction(pAction))
            return false;
    
        return gameActionSequencer.enqueue(pAction);
    }

    // TODO: enqueue overload for GameTrigger


    /**
     * Update the game state using input update
     * ---
     * run update based on specific functions within the game context
     * - gameStart
     * - playerAction
     * - animationFinish
     * - timeout from animation finish
     */
    public void update() {
        // performs a blocking game update using the next element in the updateQueue
        GameUpdate update = updateQueue.poll();
        // TODO:
        // update.perform();
    }

}
