package com.totsnuk.graveyardghouls.pojo;

import lombok.Getter;
import lombok.Setter;

@Getter
public class PlayerSession extends Session {
    public static final int PLAYER_SESSION_INACTIVE_DURATION_IN_MIN = 2;
    /**
     * The game in which this player is currently connected to
     */
    private @Setter long curGameId;

    public PlayerSession(String playerId) {
        super(playerId);
    }
}
