package com.totsnuk.graveyardghouls.state;

public enum GameActionSequencerState {
    IDLE, // no timer running
    WAITING, // timer running, waiting for more actions
    RESOLVING // currently processing stack
}
