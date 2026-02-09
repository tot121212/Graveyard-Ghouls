package com.totsnuk.graveyardghouls.service;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.totsnuk.graveyardghouls.pojo.GameSession;

import lombok.NoArgsConstructor;

@Service
@NoArgsConstructor
public class GameSessionService implements SessionService<GameSession> {
    private final Map<String, GameSession> sessions = new ConcurrentHashMap<>();

    @Override
    public GameSession createSession() {
        String id = UUID.randomUUID().toString();
        GameSession session = new GameSession(id);
        sessions.put(id, session);
        return session;
    }

    @Override
    public GameSession getSession(String id) {
        return sessions.get(id);
    }

    @Override
    public void removeSession(String id) {
        sessions.remove(id);
    }

    @Override
    public Set<GameSession> getAllSessions() {
        return new HashSet<>(sessions.values());
    }

    public int getSessionMapSize() {
        return sessions.size();
    }
}
