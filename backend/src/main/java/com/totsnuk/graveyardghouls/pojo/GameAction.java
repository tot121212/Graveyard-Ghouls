package com.totsnuk.graveyardghouls.pojo;

import com.totsnuk.graveyardghouls.enums.GameActionEvent;

public interface GameAction extends Action<GameActionEvent> {
    Player getPlayer();

    GameActionDescriptor getDescriptor();
}