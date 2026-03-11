package com.totsnuk.graveyardghouls.pojo;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.totsnuk.graveyardghouls.events.InterruptState;

import lombok.Getter;

/**
 * Manages the sequencing and execution of game actions with support for both
 * realtime and static action queues.
 * This class maintains a state machine that coordinates the timing and dispatch
 * of actions, ensuring proper
 * synchronization between client requests and server-side game logic. It
 * handles realtime actions with a
 * configurable timeout mechanism and provides event emission capabilities for
 * state transitions.
 */
@Getter
public class GameActionSequencer {
    private static final int REALTIME_TIMER_TIME = 3;

    private final GameState gameState;

    private final BlockingDeque<GameAction> realtimeStack;
    private final BlockingQueue<GameAction> staticQueue;

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public GameActionSequencer(GameState gameState) {
        this.gameState = gameState;
        this.realtimeStack = gameState.getRealtimeStack();
        this.staticQueue = gameState.getStaticQueue();
    }

    public boolean clear() {
        if (gameState.getInterruptState() != InterruptState.INACTIVE)
            return false;
        realtimeStack.clear();
        staticQueue.clear();
        return true;
    }

    /**
     * @return If a static action exists within the queue
     */
    public boolean hasStatic() {
        return !staticQueue.isEmpty();
    }

    /**
     * 
     * @return If we are currently in the realtime mode
     */
    public boolean isRealtime() {
        return !realtimeStack.isEmpty();
    }

    private void triggerRealtimeTimer() {
        // we create a seperate thread that is a timer for 3 seconds,
        // then trigger callback to change a boolean saying isResolveable

        // Schedule for 3 seconds later
        scheduler.schedule(() -> {
            System.out.println("Timeout reached! Resolving stack.");

            gameState.setInterruptState(InterruptState.RESOLVING);

        }, REALTIME_TIMER_TIME, TimeUnit.SECONDS);
    }

    public synchronized boolean enqueue(GameAction action) {
        if (action.getDescriptor().isRealtime())
            return addRealtime(action);
        else
            return addStatic(action);
    }

    private boolean addStatic(GameAction action) {
        if (gameState.getInterruptState() != InterruptState.INACTIVE || isRealtime())
            return false;
        staticQueue.add(action);
        return true;
    }

    /**
     * Adds realtime actions to the stack <br>
     * Triggers realtime if not already triggered <br>
     * Ensures that actions happen in the proper state
     */
    private boolean addRealtime(GameAction action) {
        switch (gameState.getInterruptState()) {
            case InterruptState.INACTIVE -> {
                gameState.setInterruptState(InterruptState.WAITING);

                staticQueue.clear();
                realtimeStack.add(action);
                triggerRealtimeTimer();
            }
            case InterruptState.WAITING -> {
                realtimeStack.add(action);
            }
            case InterruptState.RESOLVING -> {
                return false;
            }
            default -> {
                throw new IllegalStateException("Must validate state");
            }
        }
        staticQueue.clear();
        realtimeStack.add(action);
        return true;
    }

}