package com.totsnuk.graveyardghouls.service;

import java.util.Map;

import com.totsnuk.graveyardghouls.pojo.Session;

public interface SessionService {

    public String createSession(String id);

    public boolean hasSession(String token);

    public Session getSession(String token);

    public Map<String, Session> getAllSessions(); // ConcurrentHashMap

    public void removeSession(String token);
}
