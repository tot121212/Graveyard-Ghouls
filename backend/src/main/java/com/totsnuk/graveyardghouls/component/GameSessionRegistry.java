package com.totsnuk.graveyardghouls.component;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.totsnuk.graveyardghouls.pojo.GameSession;

@Component
public class GameSessionRegistry implements SessionRegistry<GameSession> {
    /** SessionId to Session */
    private final Map<String, GameSession> sessions = new ConcurrentHashMap<>();

    @Override
    public GameSession add(GameSession session) {
        if (session == null || session.getId() == null) {
            throw new IllegalArgumentException("PlayerSession or id cannot be null");
        }
        sessions.put(session.getId(), session);
        return session;
    }

    @Override
    public void remove(String id) {
        sessions.remove(id);
    }

    @Override
    public GameSession get(String id) {
        return sessions.get(id);
    }

    @Override
    public Collection<GameSession> getAll() {
        return sessions.values();
    }

    @Override
    public boolean contains(String id) {
        return sessions.containsKey(id);
    }

    @Override
    public int size() {
        return sessions.size();
    }
}
