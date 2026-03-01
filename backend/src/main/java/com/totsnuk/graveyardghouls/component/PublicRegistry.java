package com.totsnuk.graveyardghouls.component;

import java.util.Collection;

import com.totsnuk.graveyardghouls.pojo.ManagedEntity;

/**
 * Represents a collection of active sessions
 */
public interface PublicRegistry<T extends ManagedEntity> {

    public T add(T session);

    public T get(String id);

    public void remove(String id);

    public Collection<T> getAll();

    public boolean contains(String id);

    public int size();
}
