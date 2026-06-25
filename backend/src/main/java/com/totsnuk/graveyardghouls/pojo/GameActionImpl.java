package com.totsnuk.graveyardghouls.pojo;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.totsnuk.graveyardghouls.dto.GameActionDto;
import com.totsnuk.graveyardghouls.enums.GameActionType;
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
    public static final Map<GameActionType, GameActionDescriptor> elementToDescriptor = new ConcurrentHashMap<>();

    /**
     * Creates GameAction from GameActionDto
     * 
     * @param dto
     * @return
     */
    public static GameAction from(GameActionDto dto) {
        if (dto == null)
            return null;

        Enum<?> e = dto.getElement();
        if (e == null || !(e instanceof GameActionType))
            return null;
        GameActionType actionEnum = (GameActionType) e;
        Player player = dto.getPlayer();
        Record payload = dto.getPayload();

        if (player == null || payload == null)
            return null;

        // find descriptor from map
        GameActionDescriptor descriptor = elementToDescriptor.get(actionEnum);
        if (descriptor == null)
            return null;

        return new GameActionImpl(actionEnum, player, payload, descriptor);
    }

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

    /**
     * Object with metadata describing the action
     */
    private final GameActionDescriptor descriptor;
}
