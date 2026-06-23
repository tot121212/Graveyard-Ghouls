package com.totsnuk.graveyardghouls.events;

public enum GameEventType implements Event{
    CARD_PLAYED,
    UNIT_CREATED,
    UNIT_ENTERED_BATTLEFIELD,
    UNIT_LEFT_BATTLEFIELD,
    ATTACK_DECLARED,
    DAMAGE_DEALT,
    UNIT_DIED
}
