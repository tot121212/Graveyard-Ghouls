package com.totsnuk.graveyardghouls.pojo;

import com.totsnuk.graveyardghouls.events.Event;
import com.totsnuk.graveyardghouls.events.EventDispatcher;
import com.totsnuk.graveyardghouls.events.GameLifecycleState;

import lombok.RequiredArgsConstructor;

/**
 * Stores and handles changes to game lifecycle,
 * sends events accordingly
 */
@RequiredArgsConstructor
public class GameLifecycleHandler {
    private final EventDispatcher<Event> eventBus;
    private GameLifecycleState state = GameLifecycleState.LOBBY;

    public boolean set(GameLifecycleState state) {
        if (state != null && state instanceof GameLifecycleState) {
            this.state = state;
            eventBus.emit(state, null);
        }

        return false;
    }
}
