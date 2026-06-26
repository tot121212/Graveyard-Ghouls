package com.totsnuk.graveyardghouls.pojo;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.totsnuk.graveyardghouls.events.EventDispatcher;
import com.totsnuk.graveyardghouls.events.GameEvent;
import com.totsnuk.graveyardghouls.events.PlayerAction;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
public class Game {
    private final EventDispatcher eventBus = new EventDispatcher();
    private final GameState gameState = new GameState(eventBus);
    private final PlayerRegistry playerRegistry = new PlayerRegistry(eventBus);

    private final GameLifecycleHandler lifecycleHandler = new GameLifecycleHandler(gameState, eventBus);
    private final GameEventSequencer gameEventSequencer = new GameEventSequencer(gameState);

    private final SeatRegistry seatRegistry = new SeatRegistry();

    private final Queue<GameUpdate> updateQueue = new ConcurrentLinkedQueue<>();

    public void reset() {
        this.gameEventSequencer.clear();
        this.playerRegistry.clear();
        // shouldn't do this -V- because we want to return to lobby after game ends
        // this.seatRegistry.reset();
    }

    private boolean isValidPlayerAction(PlayerAction action) {
        if (action == null || !playerRegistry.contains(action.getPlayer()))
            return false;

        return action.getDescriptor().isRealtime() || playerRegistry.isPlayerTurn(action.getPlayer());
    }

    /**
     * - Adds a game action to the queue
     * - This should perform all necessary validation before adding to the queue
     * - Also interacts with the RealtimeStack
     * ---
     * This style of enqueue ensures that currentPlayer can play actions
     * sequentially without waiting for animations to finish
     */
    public synchronized boolean enqueue(GameEvent<?> gameEvent) {
        if (gameEvent == null)
            return false;
        
        return gameEventSequencer.enqueue(gameEvent);
    }

    public synchronized boolean enqueue(PlayerAction pAction){
        if (pAction == null
        || !isValidPlayerAction(pAction))
            return false;
        
        return gameEventSequencer.enqueue(pAction);
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
        // update.perform();
    }

}
