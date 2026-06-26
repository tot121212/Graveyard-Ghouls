package com.totsnuk.graveyardghouls.enums;

public enum StackState {
    INACTIVE, // no timer running
    WAITING, // timer running, waiting for more actions
    RESOLVING // currently processing stack
}
