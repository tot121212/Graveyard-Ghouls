package com.totsnuk.graveyardghouls.events;

public enum InterruptState implements Event {
    IDLE, // no timer running
    WAITING, // timer running, waiting for more actions
    RESOLVING // currently processing stack
}
