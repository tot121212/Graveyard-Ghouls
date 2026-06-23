package com.totsnuk.graveyardghouls.pojo;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

import com.totsnuk.graveyardghouls.events.Event;
import com.totsnuk.graveyardghouls.events.EventDispatcher;
import com.totsnuk.graveyardghouls.events.InterruptState;
import com.totsnuk.graveyardghouls.events.LifecycleState;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Stores all game state </br>
 * Allows for easy creation of updates for client
 */
@Getter
@RequiredArgsConstructor
public class GameState {
    private final EventDispatcher<Event> eventBus;

    /**
     * Lifecycle of game
     */
    private LifecycleState lifecycle = LifecycleState.LOBBY;
    
    public void setLifecycle(LifecycleState lifecycle) {
        this.lifecycle = lifecycle;
        eventBus.emit(this.lifecycle);
    }
    
    /**
     * State of interrupt
     */
    private InterruptState interruptState = InterruptState.INACTIVE;
    
    public void setInterruptState(InterruptState state) {
        this.interruptState = state;
        eventBus.emit(this.interruptState);
    }

    /**
     * When the realtime stack has elements, the gameLoop will wait for 3 seconds
     * for the clients to send any addtional Realtimes
     */
    private final BlockingDeque<GameAction> realtimeStack = new LinkedBlockingDeque<>();

    /**
     * For now it will be limited to one element for ease of production
     */
    private final BlockingQueue<GameAction> staticQueue = new LinkedBlockingQueue<>();
}
