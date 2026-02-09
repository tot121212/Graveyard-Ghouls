package com.totsnuk.graveyardghouls.pojo;

import java.util.HashSet;
import java.util.Set;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class GameSession extends Session {
    /**
     * Duration in which session is allowed to be active before deletion
     */
    public static final int GAME_SESSION_INACTIVE_DURATION_IN_MIN = 2;
    /**
     * Set of players that are currently connected
     */
    public final Set<PlayerSession> connectedPlayers = new HashSet<>();

    public GameSession(String gameId) {
        super(gameId);
    }

    public boolean playerJoin(PlayerSession playerSession) {
        if (this.connectedPlayers.contains(playerSession)) {
            log.info("Player tried to connect but was already connected");
            return false;
        }
        this.connectedPlayers.add(playerSession);
        playerSession.setCurGame(this);
        // TODO: add logic here probably
        return true;
    }

    public boolean playerLeave(PlayerSession playerSession) {
        boolean result = this.connectedPlayers.remove(playerSession);
        playerSession.setCurGame(null);
        // TODO: add logic here probably
        return result;
    }
}
