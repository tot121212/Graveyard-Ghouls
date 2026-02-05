package com.totsnuk.graveyardghouls.service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.totsnuk.graveyardghouls.pojo.PlayerSession;
import com.totsnuk.graveyardghouls.pojo.Session;

@Service
public class PlayerSessionService implements SessionService {
    private final Map<String, Session> sessions = new ConcurrentHashMap<>();

    @Override
    public String createSession(String playerId) {
        String token = UUID.randomUUID().toString();
        sessions.put(token, new PlayerSession(playerId));
        return token;
    }

    @Override
    public boolean hasSession(String token) {
        return sessions.containsKey(token);
    }

    @Override
    public PlayerSession getSession(String token) {
        return (PlayerSession) sessions.get(token);
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
