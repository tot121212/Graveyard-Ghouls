package com.totsnuk.graveyardghouls.pojo;

import com.totsnuk.graveyardghouls.enums.GameActionType;

public interface GameAction extends Action<GameActionType> {
    Player getPlayer();
    GameActionDescriptor getDescriptor();
}