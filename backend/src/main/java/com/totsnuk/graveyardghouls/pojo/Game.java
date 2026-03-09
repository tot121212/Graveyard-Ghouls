package com.totsnuk.graveyardghouls.pojo;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.totsnuk.graveyardghouls.dto.GameActionDto;
import com.totsnuk.graveyardghouls.events.Event;
import com.totsnuk.graveyardghouls.events.EventDispatcher;

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
    private final AnimationHandler animationHandler = new AnimationHandler(this);

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

    private boolean isValidAction(GameAction action) {
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
    public synchronized boolean enqueue(GameActionDto dto) {
        if (dto == null)
            return false;

        GameAction action = GameActionImpl.from(dto);
        if (action == null)
            return false;

        if (!isValidAction(action))
            return false;

        return gameActionSequencer.enqueue(action);
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

    public void onAnimate(Animation animation, long ms, long timePlayed) {
        // TODO:
    }
}
