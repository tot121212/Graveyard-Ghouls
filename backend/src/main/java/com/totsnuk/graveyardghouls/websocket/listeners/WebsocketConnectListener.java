package com.totsnuk.graveyardghouls.websocket.listeners;

import java.util.Map;

import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;

import com.totsnuk.graveyardghouls.component.GameSessionRegistry;
import com.totsnuk.graveyardghouls.enums.GameCookies;
import com.totsnuk.graveyardghouls.pojo.GameSession;
import com.totsnuk.graveyardghouls.pojo.Participant;

import lombok.extern.slf4j.Slf4j;

/**
 * Handles storing gameSession and participant for websocket connection
 */
@Slf4j
@Component
public class WebsocketConnectListener implements ApplicationListener<SessionConnectEvent> {
    private GameSessionRegistry gameSessionRegistry;

    @Override
    public void onApplicationEvent(SessionConnectEvent event) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
        log.info("Spring STOMP session connected");
        Map<String, Object> sAttrs = sha.getSessionAttributes();

        String gid = (String) sAttrs.get(GameCookies.GAME_SESSION_ID);
        String publicId = (String) sAttrs.get(GameCookies.PUBLIC_ID);
        String privateToken = (String) sAttrs.get(GameCookies.PRIVATE_TOKEN);
        if (gid == null || publicId == null || privateToken == null)
            return;

        GameSession gameSession = gameSessionRegistry.get(gid);
        if (gameSession == null)
            return;

        Participant participant =
                gameSession.getParticipantRegistry().getByPrivate(publicId, privateToken);
        if (participant == null)
            return;

        // Store participant and gameSession on the websocket session for easy routing
        // NOTE: if we decide to containerize the game server itself, we can do that
        // without issue.
        sAttrs.put("participant", participant);
        sAttrs.put("gameSession", gameSession);
    }
}
