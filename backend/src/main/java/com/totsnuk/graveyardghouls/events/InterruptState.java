package com.totsnuk.graveyardghouls.events;

public enum InterruptState implements Event {
    INACTIVE, // no timer running
    WAITING, // timer running, waiting for more actions
    RESOLVING // currently processing stack
}
