package com.totsnuk.graveyardghouls.pojo;

import com.totsnuk.graveyardghouls.enums.GameActionEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * - what action to perform
 * - what player is performing it
 * - what information is being sent from the client about said action
 * - Must be constructed externally and sent to the gameQueue as a clean input
 */
@Getter
@AllArgsConstructor
public class GameActionImpl implements GameAction {
    /**
     * The action they are performing
     */
    private final Enum<GameActionEnum> element;
    /**
     * The player executing the action
     */
    private final Player player;
    /**
     * The payload associated
     */
    private final Record payload;

    /**
     * Object with metadata describing the action
     */
    private final GameActionDescriptor descriptor;
}
