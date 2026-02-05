package com.totsnuk.graveyardghouls.pojo;

import java.time.Instant;

import lombok.Getter;

@Getter
public abstract class Session {
    private final String id;
    private final Instant createdAt;
    private volatile Instant lastActivity;

    protected Session(String id) {
        this.id = id;
        this.createdAt = Instant.now();
        this.lastActivity = Instant.now();
    }

    public void updateLastActivity() {
        this.lastActivity = Instant.now();
    }
}
