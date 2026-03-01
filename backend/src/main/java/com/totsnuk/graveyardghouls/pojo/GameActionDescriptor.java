package com.totsnuk.graveyardghouls.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Describes action metadata relevant to the action its mapped to
 */
@AllArgsConstructor
@Getter
public abstract class GameActionDescriptor {
    private final boolean isRealtime;
    private final boolean requiresTarget;
}
