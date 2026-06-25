package com.totsnuk.graveyardghouls.events;

import com.totsnuk.graveyardghouls.enums.GameTriggerType;
import com.totsnuk.graveyardghouls.pojo.GameActionDescriptor;
import com.totsnuk.graveyardghouls.pojo.Player;

/**
 * Trigger within game for events such as permanents on the board or spells that have lasting effects
 */
public interface GameTrigger extends GameEvent<GameTriggerType>{
    Player getPlayer();
    GameActionDescriptor getDescriptor();
}
