package com.totsnuk.graveyardghouls.pojo;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

import com.totsnuk.graveyardghouls.enums.LifecycleState;
import com.totsnuk.graveyardghouls.enums.StackState;
import com.totsnuk.graveyardghouls.events.EventDispatcher;
import com.totsnuk.graveyardghouls.events.GameEvent;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Stores all game state </br>
 * Allows for easy creation of updates for client
 */
@Getter
@RequiredArgsConstructor
public class GameState {
    private final EventDispatcher eventBus;

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
    private StackState stackState = StackState.INACTIVE;
    
    public void setStackState(StackState state) {
        this.stackState = state;
        eventBus.emit(this.stackState);
    }

    /**
     * When the realtime stack has specific elements, the gameLoop will wait for 3 seconds for the </br>
     * clients to send any addtional Realtimes </br>
     * This is dictated by certain parameters such as when someone actually has something they </br>
     * are able to do on someone elses turn, etc.
     */
    private final BlockingDeque<GameEvent<?>> eventStack = new LinkedBlockingDeque<>();

    /**
     * Record type for storage of game state
     */
    public record GameStateRecord(
        LifecycleState lifecycle,
        StackState stackState,
        BlockingDeque<GameEvent<?>> eventStack
    ){};
    
    /**
     * Get method used for getting an instance of the currently held state of the game
     */
    private GameStateRecord get(){
        return new GameStateRecord(
            this.lifecycle,
            this.stackState,
            this.eventStack
        );
    }
}
