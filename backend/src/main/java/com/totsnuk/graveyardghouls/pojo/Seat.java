package com.totsnuk.graveyardghouls.pojo;

import lombok.Getter;
import lombok.Setter;

/**
 * - Represents a slot within the game, to be filled by a participant
 * 
 * - When the game starts, If the seat isnt filled with a GameConnection; the
 * seat will be excluded
 */
@Setter
@Getter
public class Seat {
    /**
     * Participant that this seat is associated to at the moment
     */
    private Participant participant;
    /**
     * Display name associated with seat/player
     */
    private String displayName;

    /**
     * Whether the occupant is ready for the game to start
     */
    private boolean ready = false;

    public boolean setDisplayName(String displayName) {
        if (displayName == null || displayName.isEmpty() || displayName.isBlank()) {
            return false;
        }

        this.displayName = displayName;
        return true;
    }

    public boolean isEmpty() {
        return this.participant == null;
    }
}
