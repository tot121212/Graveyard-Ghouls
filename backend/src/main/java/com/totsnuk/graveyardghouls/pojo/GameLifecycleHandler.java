package com.totsnuk.graveyardghouls.pojo;

import com.totsnuk.graveyardghouls.events.Event;
import com.totsnuk.graveyardghouls.events.EventDispatcher;
import com.totsnuk.graveyardghouls.events.LifecycleState;

import lombok.RequiredArgsConstructor;

/**
 * Stores and handles changes to game lifecycle,
 * sends events accordingly
 */
@RequiredArgsConstructor
public class GameLifecycleHandler {
    private final GameState gameState;
    private final EventDispatcher<Event> eventBus;

    public boolean set(LifecycleState state) {
        if (state != null && state instanceof LifecycleState) {
            gameState.setLifecycle(state);
            eventBus.emit(state);
            return true;
        }
        return false;
    }
}
