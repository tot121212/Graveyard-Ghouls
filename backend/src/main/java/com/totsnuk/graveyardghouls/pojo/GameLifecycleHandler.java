package com.totsnuk.graveyardghouls.pojo;

import com.totsnuk.graveyardghouls.enums.LifecycleState;
import com.totsnuk.graveyardghouls.events.EventDispatcher;

import lombok.RequiredArgsConstructor;

/**
 * Stores and handles changes to game lifecycle,
 * sends events accordingly
 */
@RequiredArgsConstructor
public class GameLifecycleHandler {
    private final GameState gameState;
    private final EventDispatcher eventBus;

    public boolean set(LifecycleState state) {
        if (state != null && state instanceof LifecycleState) {
            gameState.setLifecycle(state);
            eventBus.emit(state);
            return true;
        }
        return false;
    }
}
