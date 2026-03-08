package com.totsnuk.graveyardghouls.pojo;

import com.totsnuk.graveyardghouls.events.GameActionEvent;

public interface GameAction extends Action<GameActionEvent> {
    Player getPlayer();

    GameActionDescriptor getDescriptor();
}