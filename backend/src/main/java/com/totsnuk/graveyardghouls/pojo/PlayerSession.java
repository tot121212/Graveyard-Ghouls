package com.totsnuk.graveyardghouls.pojo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerSession extends Session {
    /**
     * Duration in which session is allowed to be active before deletion
     */
    public static final int PLAYER_SESSION_INACTIVE_DURATION_IN_MIN = 2;
    /**
     * Used to authenticate game actions by the game
     */
    private final String privateId;
    /**
     * The game in which this player is currently connected to
     */
    private GameSession curGame;

    public PlayerSession(String playerId, String privateId) {
        super(playerId);
        this.privateId = privateId;
    }
}
