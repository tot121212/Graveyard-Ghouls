package com.totsnuk.graveyardghouls.pojo;

import com.totsnuk.graveyardghouls.events.LifecycleState;

import lombok.RequiredArgsConstructor;

/**
 * Stores and handles changes to game lifecycle,
 * sends events accordingly
 */
@RequiredArgsConstructor
public class GameLifecycleHandler {
    private final Game game;
    private final GameState gameState;

    public boolean set(LifecycleState state) {
        if (state != null && state instanceof LifecycleState) {
            gameState.setLifecycle(state);
            gameState.onSetLifecycleState(state);
        }

        return false;
    }
}
