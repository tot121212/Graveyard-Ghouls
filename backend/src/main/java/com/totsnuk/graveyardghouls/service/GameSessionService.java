package com.totsnuk.graveyardghouls.service;

import org.springframework.stereotype.Service;

import com.totsnuk.graveyardghouls.component.GameSessionRegistry;
import com.totsnuk.graveyardghouls.dto.JoinDto;
import com.totsnuk.graveyardghouls.pojo.GameSession;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Handles the storage of and interaction with the game session
 */
@Service
@AllArgsConstructor
@Slf4j
public class GameSessionService {
    private final GameSessionRegistry gameSessionRegistry;

    /**
     * @return Session id for game
     */
    public String create() {
        return gameSessionRegistry.add(new GameSession()).getId();
    }

    public JoinDto join(String gid) {
        return join(gid, null, null);
    }

    /**
     * - Triggered when a user requests to join a game session
     * 
     * @return Player ID for this particular user in the session
     */
    public JoinDto join(String gid, @Nullable String participantId, @Nullable String privateToken) {

        // find game session
        GameSession gameSession = gameSessionRegistry.get(gid);
        if (gameSession == null)
            return null;
        if (participantId == null || privateToken == null) {
            return gameSession.connect();
        }
        return gameSession.reconnect(participantId, privateToken);
    }

    /**
     * Triggered when user requests to leave a game session
     * 
     * @param gid
     * @param pid
     */
    public boolean leave(String gid, String pid, String privateToken) {
        GameSession gameSession = gameSessionRegistry.get(gid);
        if (gameSession == null) {
            log.warn("gameSession not found");
            return false;
        }
        return gameSession.disconnect(pid, privateToken);
    }
}
