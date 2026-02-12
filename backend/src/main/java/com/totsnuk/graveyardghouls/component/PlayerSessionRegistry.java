package com.totsnuk.graveyardghouls.component;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.totsnuk.graveyardghouls.pojo.PlayerSession;

@Component
public class PlayerSessionRegistry implements SessionRegistry<PlayerSession> {
    /**SessionId to Session*/
    private final Map<String, PlayerSession> sessions = new ConcurrentHashMap<>();

    @Override
    public PlayerSession add(PlayerSession session) {
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
    public PlayerSession get(String id) {
        return sessions.get(id);
    }

    @Override
    public boolean contains(String id) {
        return sessions.containsKey(id);
    }

    @Override
    public Collection<PlayerSession> getAll() {
        return sessions.values();
    }

    @Override
    public int size() {
        return sessions.size();
    }
}
