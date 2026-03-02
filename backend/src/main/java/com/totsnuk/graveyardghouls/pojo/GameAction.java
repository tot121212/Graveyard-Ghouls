package com.totsnuk.graveyardghouls.pojo;

import com.totsnuk.graveyardghouls.enums.GameActionEnum;

public interface GameAction extends Action<GameActionEnum> {
    Player getPlayer();

    GameActionDescriptor getDescriptor();
}