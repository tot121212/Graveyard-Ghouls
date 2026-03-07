package com.totsnuk.graveyardghouls.pojo;

import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.totsnuk.graveyardghouls.event.EventDispatcher;
import com.totsnuk.graveyardghouls.state.InterruptState;

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
public class GameActionSequencer {
    private static final int REALTIME_TIMER_TIME = 3;
    /**
     * When the realtime stack has elements, the gameLoop will wait for 3 seconds
     * for the clients to send any addtional Realtimes
     */
    private final BlockingQueue<GameAction> realtimeStack = new LinkedBlockingDeque<>();
    /**
     * For now it will be limited to one element for ease of production
     */
    private final Queue<GameAction> staticQueue = new LinkedBlockingQueue<>();

    public final EventDispatcher<InterruptState> eventDispatcher = new EventDispatcher<>();

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    private InterruptState state = InterruptState.IDLE;

    public boolean clear() {
        if (state != InterruptState.IDLE)
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

    public boolean addStatic(GameAction action) {
        if (state != InterruptState.IDLE)
            return false;
        // if realtimeStack has an action, static action queue is locked
        if (isRealtime())
            return false;
        staticQueue.add(action);
        return true;
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

            state = InterruptState.RESOLVING;
            eventDispatcher.emit(state, null);

        }, REALTIME_TIMER_TIME, TimeUnit.SECONDS);
    }

    /**
     * Adds realtime actions to the stack <br>
     * Triggers realtime if not already triggered <br>
     * Ensures that actions happen in the proper state
     */
    public synchronized boolean addRealtime(GameAction action) {
        switch (state) {
            case InterruptState.IDLE -> {
                state = InterruptState.WAITING;
                eventDispatcher.emit(state, null);

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