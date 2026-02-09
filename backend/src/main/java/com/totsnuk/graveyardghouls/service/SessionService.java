package com.totsnuk.graveyardghouls.service;

import java.util.Set;

import com.totsnuk.graveyardghouls.pojo.Session;

public interface SessionService<T extends Session> {

    public T createSession();

    public T getSession(String id);

    public void removeSession(String id);

    public Set<T> getAllSessions();
}
