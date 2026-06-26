package com.totsnuk.graveyardghouls.pojo;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.totsnuk.graveyardghouls.enums.GameSettings;
import com.totsnuk.graveyardghouls.enums.StackState;
import com.totsnuk.graveyardghouls.events.GameEvent;

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
public class GameEventSequencer {
    // TODO: probably need the event bus to send signals to triggers to be added to the realtime queue
    private final GameState gameState;

    private final BlockingDeque<GameEvent<?>> eventStack;

    private final ScheduledExecutorService stackResolveScheduler = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> stackResolveFuture;

    //Pull event stack from state
    public GameEventSequencer(GameState gameState) {
        this.gameState = gameState;
        this.eventStack = gameState.getEventStack();
    }

    public void clear() {
        stackResolveFuture.cancel(interruptStackResolveWhenTriggeringAgain);
        eventStack.clear();
    }

    /** TODO: figure out what "cancelling an already running task" actually means */
    private final boolean interruptStackResolveWhenTriggeringAgain = false;
    private void triggerStackResolveTimeout() {
        stackResolveFuture.cancel(interruptStackResolveWhenTriggeringAgain);
        stackResolveFuture = stackResolveScheduler.schedule(() -> {
            System.out.println("Timeout reached! Resolving stack.");
            if (gameState.getStackState() == StackState.WAITING)
                gameState.setStackState(StackState.RESOLVING);
            /** TODO: trigger eventBus for stackResolveTimeoutStarted */
        }, GameSettings.STACK_RESOLUTION_TIME, TimeUnit.SECONDS);
    }

    /**
     * Adds actions to the stack <br>
     * Triggers stack resolution timeout if not already triggered <br>
     * Ensures that actions happen in the proper state
     */
    public boolean enqueue(GameEvent<?> event) {
        switch (gameState.getStackState()) {
            case StackState.INACTIVE -> {
                gameState.setStackState(StackState.WAITING);
                triggerStackResolveTimeout();
            }
            case StackState.WAITING -> {
            }
            case StackState.RESOLVING -> {
                System.out.println("Cannot enqueue action whilst StackState is resolving");
                return false;
            }
            default -> {
                throw new IllegalStateException("Must validate state");
            }
        }
        eventStack.add(event);
        return true;
    }
}