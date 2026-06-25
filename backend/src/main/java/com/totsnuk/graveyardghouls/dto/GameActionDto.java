package com.totsnuk.graveyardghouls.dto;

import com.totsnuk.graveyardghouls.enums.GameActionType;
import com.totsnuk.graveyardghouls.pojo.Player;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class GameActionDto {
    /**
     * The action they are performing
     */
    private final GameActionType element;
    /**
     * The player executing the action
     */
    private final Player player;
    /**
     * The payload associated
     */
    private final Record payload;
}
