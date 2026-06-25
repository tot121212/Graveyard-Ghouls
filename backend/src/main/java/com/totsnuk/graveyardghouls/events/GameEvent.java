package com.totsnuk.graveyardghouls.events;

/**
 * Signifies the template for any event which occurs within the game
 * Is extended via enum value
 */
public interface GameEvent<E extends Enum<E>> {
    Enum<?> getElement();
    Record getPayload();
}
