package com.totsnuk.graveyardghouls.pojo;

import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.totsnuk.graveyardghouls.state.EventDispatcher;
import com.totsnuk.graveyardghouls.state.GameActionSequencerState;

/**
 * Handles the sequence of actions
 */
public class GameActionSequencer {
    private GameActionSequencerState state = GameActionSequencerState.IDLE;
    /**
     * When the realtime stack has elements, the gameLoop will wait for 3 seconds
     * for the clients to send any addtional Realtimes
     */
    private final BlockingQueue<GameAction> realtimeStack = new LinkedBlockingDeque<>();
    /**
     * For now it will be limited to one element for ease of production
     */
    private final Queue<GameAction> staticQueue = new LinkedBlockingQueue<>();

    public final EventDispatcher<GameActionSequencerState> eventDispatcher = new EventDispatcher<>();

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public boolean clear() {
        if (state != GameActionSequencerState.IDLE)
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
        if (state != GameActionSequencerState.IDLE)
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

    private static final int REALTIME_TIMER_TIME = 3;

    private void triggerRealtimeTimer() {
        // we create a seperate thread that is a timer for 3 seconds,
        // then trigger callback to change a boolean saying isResolveable

        Runnable callback = () -> {
            state = GameActionSequencerState.RESOLVING;
            System.out.println("Timeout reached! Executing callback.");
            eventDispatcher.emit(state, null);
        };

        // Schedule for 3 seconds later
        scheduler.schedule(callback, REALTIME_TIMER_TIME, TimeUnit.SECONDS);
    }

    /**
     * Adds realtime actions to the stack <br>
     * Triggers realtime if not already triggered
     */
    public synchronized boolean addRealtime(GameAction action) {
        switch (state) {
            case GameActionSequencerState.IDLE -> {
                state = GameActionSequencerState.WAITING;
                staticQueue.clear();
                realtimeStack.add(action);
                triggerRealtimeTimer();
            }
            case GameActionSequencerState.WAITING -> {
                realtimeStack.add(action);
            }
            case GameActionSequencerState.RESOLVING -> {
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

    /**
     * To be overwritten with what you want to do upon resolve
     * 
     * @return
     */
    public boolean resolveStaticQueue() {
        return false;
    }

}