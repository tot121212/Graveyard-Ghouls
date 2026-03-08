package com.totsnuk.graveyardghouls.pojo;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.totsnuk.graveyardghouls.dto.GameActionDto;
import com.totsnuk.graveyardghouls.events.Event;
import com.totsnuk.graveyardghouls.events.EventDispatcher;
import com.totsnuk.graveyardghouls.events.GameActionEvent;
import com.totsnuk.graveyardghouls.events.InterruptState;

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
     * action, and Realtimes are only accepted during explicit windows with the
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

    private final EventDispatcher<Event> eventBus = new EventDispatcher<>();

    private final GameLifecycleHandler lifecycleHandler = new GameLifecycleHandler(eventBus);
    private final GameActionSequencer gameActionSequencer = new GameActionSequencer(eventBus);
    private final AnimationHandler animationHandler = new AnimationHandler(eventBus);

    private final SeatRegistry seatRegistry = new SeatRegistry();

    private final Queue<GameUpdate> updateQueue = new ConcurrentLinkedQueue<>();

    private final List<Player> players = new ArrayList<>();

    private Player currentPlayer;

    public void init() {
        link();
        reset();
    }

    public void link() {
        eventBus.onEvent(InterruptState.WAITING, this::onInterruptStart);
    }

    public void reset() {
        this.gameActionSequencer.clear();
        this.players.clear();
        // shouldn't do this -V- because we want to return to lobby after game ends
        // this.seatRegistry.reset();
    }

    public boolean isPlayerTurn(Player player) {
        return player == currentPlayer;
    }

    /**
     * Conditions:
     * - isnt null
     * - is player within players list
     */
    private boolean isValidAction(GameAction action) {
        return action != null
                && players.contains(action.getPlayer());
    }

    /**
     * Conditions:
     * - realtime stack isnt active
     * - player isnt null
     * - is players turn
     * - player doesnt have action enqueued already
     */
    private boolean isValidStaticAction(GameAction action) {
        Player player = action.getPlayer();

        return !gameActionSequencer.isRealtime()
                && player != null
                && isPlayerTurn(player)
                && gameActionSequencer.hasStatic();
    }

    /**
     * Conditions:
     * - descriptor isnt null
     * - descriptor is realtime
     */
    private boolean isValidRealtimeAction(GameAction action) {
        GameActionDescriptor descriptor = action.getDescriptor();

        return descriptor != null
                && descriptor.isRealtime();
    }

    /**
     * - Adds a game action to the queue
     * - This should perform all necessary validation before adding to the queue
     * - Also interacts with the RealtimeStack
     * ---
     * This style of enqueue ensures that currentPlayer can play actions
     * sequentially without waiting for animations to finish
     */
    public synchronized boolean enqueue(GameActionDto dto) {
        if (dto == null)
            return false;

        GameAction action = GameActionImpl.from(dto);
        if (action == null)
            return false;
        if (!isValidAction(action))
            return false;

        GameActionEvent actionEnum = action.getElement();
        Player player = action.getPlayer();
        Record payload = action.getPayload();

        if (player == null || payload == null || actionEnum == null)
            return false;

        if (isValidStaticAction(action)) {
            // enqueue action normally
            if (!gameActionSequencer.addStatic(action))
                return false;
            // TODO: update stuff
            return true;
        } else if (isValidRealtimeAction(action)) {
            gameActionSequencer.addRealtime(action);
            return true;
        }
        return false;
    }

    /**
     * Triggered when the GameActionSequencer.state is set to WAITING
     * 
     * @param args
     */
    public void onInterruptStart(Record args) {
        // TODO: implement
        // what to do when interrupt starts
        // tell game to pause
        // idk
    }

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
        // perform the update
        // update.perform();
    }

}
