package com.totsnuk.graveyardghouls.pojo;

import lombok.Getter;

@Getter
public class GameSession extends Session {
    public GameSession(String gameId) {
        super(gameId);
    }
}
