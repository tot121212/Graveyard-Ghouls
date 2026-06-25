package com.totsnuk.graveyardghouls.pojo;

import java.util.ArrayList;
import java.util.List;

import com.totsnuk.graveyardghouls.events.EventDispatcher;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class PlayerRegistry {
    private final EventDispatcher eventBus;
    private final List<Player> players = new ArrayList<>();
    private Player currentPlayer = null;

    public void clear(){
        players.clear();
    }

    public boolean contains(Player player){
        return players.contains(player);
    }

    public boolean isPlayerTurn(Player player) {
        return player == currentPlayer;
    }

    public boolean playerExists(Player player) {
        return players.contains(player);
    }
}
