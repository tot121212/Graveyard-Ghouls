package com.totsnuk.graveyardghouls.events;

import com.totsnuk.graveyardghouls.enums.GameActionType;
import com.totsnuk.graveyardghouls.pojo.GameActionDescriptor;
import com.totsnuk.graveyardghouls.pojo.Player;

public interface PlayerAction extends GameEvent<GameActionType> {
    Player getPlayer();
    GameActionDescriptor getDescriptor();
}