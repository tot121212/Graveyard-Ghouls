package com.totsnuk.graveyardghouls.pojo;

import lombok.Getter;

@Getter
public class GameSession extends Session {
    public static final int GAME_SESSION_INACTIVE_DURATION_IN_MIN = 2;

    public GameSession(String gameId) {
        super(gameId);
    }
}
