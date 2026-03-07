package com.totsnuk.graveyardghouls.state;

public enum InterruptState {
    IDLE, // no timer running
    WAITING, // timer running, waiting for more actions
    RESOLVING // currently processing stack
}
