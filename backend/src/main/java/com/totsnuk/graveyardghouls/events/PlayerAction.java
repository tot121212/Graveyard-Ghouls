package com.totsnuk.graveyardghouls.events;

import com.totsnuk.graveyardghouls.enums.PlayerActionType;
import com.totsnuk.graveyardghouls.pojo.GameActionDescriptor;
import com.totsnuk.graveyardghouls.pojo.Player;

public interface PlayerAction extends GameEvent<PlayerActionType> {
    Player getPlayer();
    GameActionDescriptor getDescriptor();
}