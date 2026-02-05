package com.totsnuk.graveyardghouls.pojo;

import lombok.Getter;

@Getter
public class PlayerSession extends Session {
    public PlayerSession(String playerId) {
        super(playerId);
    }
}
