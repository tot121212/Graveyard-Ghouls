package com.totsnuk.graveyardghouls.service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.totsnuk.graveyardghouls.pojo.GameSession;
import com.totsnuk.graveyardghouls.pojo.Session;

import lombok.NoArgsConstructor;

@Service
@NoArgsConstructor
public class GameSessionService implements SessionService {
    /**
     * Map of playerId to Session
     */
    private final Map<String, Session> sessions = new ConcurrentHashMap<>();

    @Override
    public String createSession(String playerId) {
        String token = UUID.randomUUID().toString();
        sessions.put(token, new GameSession(playerId));
        return token;
    }

    @Override
    public boolean hasSession(String token) {
        return sessions.containsKey(token);
    }

    @Override
    public GameSession getSession(String token) {
        return (GameSession) sessions.get(token);
    }

    @Override
    public Map<String, Session> getAllSessions() {
        return sessions;
    }

    @Override
    public void removeSession(String token) {
        sessions.remove(token);
    }
}
