package com.totsnuk.graveyardghouls.pojo;

import java.util.UUID;

import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Dynamic bi-directional object representing the relationship between
 * connection -> seat -> player
 * When setting we need to trigger events
 */
@Getter
@Setter
@NoArgsConstructor
public class Participant extends ManagedEntity {
    private boolean isConnected = true;
    /**
     * Token given to user after each new GameConnection for interaction validity
     */
    private String privateToken;
    @Nullable
    private Seat seat;
    @Nullable
    private Player player;

    /**
     * Sets the participant connection and regenerates the token
     * 
     * @param c GameConnection
     * @return The new privateToken that was regenerated
     */
    public String onConnect() {
        this.privateToken = UUID.randomUUID().toString();
        return this.privateToken;
    }
}
