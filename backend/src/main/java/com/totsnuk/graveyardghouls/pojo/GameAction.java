package com.totsnuk.graveyardghouls.pojo;

public interface GameAction extends Action {
    Player getPlayer();

    GameActionDescriptor getDescriptor();
}