package com.totsnuk.graveyardghouls.pojo;

import com.totsnuk.graveyardghouls.events.GameActionType;

public interface GameAction extends Action<GameActionType> {
    Player getPlayer();
    GameActionDescriptor getDescriptor();
}