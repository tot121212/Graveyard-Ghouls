package com.totsnuk.graveyardghouls.dto;

import com.totsnuk.graveyardghouls.pojo.Player;
public record PlayerActionDto (
    /**
     * The action they are performing
     */
    Enum<?> element,
    /**
     * The player executing the action
     */
    Player player,
    /**
     * The payload associated
     */
    Record payload
){}
