package com.totsnuk.graveyardghouls.events;

public enum LifecycleState implements Event {
    /**
     * - Seats are open for joining
     * - Players join, change display names
     */
    LOBBY,
    /**
     * - Seats are still open but countdown is started
     * - If a seat is occupied during this state, state will revert back to LOBBY
     * - All players ready, countdown possible
     */
    READY,
    /**
     * - Seats are now locked
     * - Game started, state locked
     */
    RUNNING,
    /**
     * - Seats are still locked
     * - Freezes all inputs until all current player slots are filled
     * (used when someone disconnects)
     */
    PAUSED,
    /**
     * - Game finished
     * - Seats still locked
     */
    FINISHED
}