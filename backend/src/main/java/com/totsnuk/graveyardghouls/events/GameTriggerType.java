package com.totsnuk.graveyardghouls.events;

public enum GameTriggerType implements Event{
    TURN_STARTED,
    TURN_ENDED,
    CARD_PLAYED,
    ABILITY_ACTIVATED,
    UNIT_ENTERED_BATTLEFIELD,
    UNIT_LEFT_BATTLEFIELD,
    ATTACK_DECLARED,
    DAMAGE_DEALT,
}
