package com.totsnuk.graveyardghouls.pojo;

import java.time.Instant;
import java.util.UUID;

import lombok.Getter;

/**
 * Base class for entities whose lifecycle is managed.
 * 
 * - Holds an ID, creation timestamp, and last activity timestamp.
 */
@Getter
public abstract class ManagedEntity {
    private final String id;
    private final Instant createdAt;
    private volatile Instant lastActivity;

    protected ManagedEntity() {
        this.id = UUID.randomUUID().toString();
        this.createdAt = Instant.now();
        this.lastActivity = Instant.now();
    }

    protected ManagedEntity(String id) {
        this.id = id;
        this.createdAt = Instant.now();
        this.lastActivity = Instant.now();
    }

    public void updateLastActivity() {
        this.lastActivity = Instant.now();
    }
}
