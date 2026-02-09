package com.totsnuk.graveyardghouls.service;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.totsnuk.graveyardghouls.pojo.PlayerSession;

import lombok.NoArgsConstructor;

@Service
@NoArgsConstructor
public class PlayerSessionService implements SessionService<PlayerSession> {
    private final Map<String, PlayerSession> sessions = new ConcurrentHashMap<>();

    @Override
    public PlayerSession createSession() {
        String publicId = UUID.randomUUID().toString();
        String privateId = UUID.randomUUID().toString();
        PlayerSession session = new PlayerSession(publicId, privateId);
        sessions.put(publicId, session);
        return session;
    }

    @Override
    public PlayerSession getSession(String id) {
        return sessions.get(id);
    }

    @Override
    public void removeSession(String id) {
        sessions.remove(id);
    }

    @Override
    public Set<PlayerSession> getAllSessions() {
        return new HashSet<>(sessions.values());
    }
}
