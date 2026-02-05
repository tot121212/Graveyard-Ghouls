package com.totsnuk.graveyardghouls.websocket.listeners;

import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class WebSocketSessionDisconnectListener implements ApplicationListener<SessionDisconnectEvent> {

    @Override
    public void onApplicationEvent(SessionDisconnectEvent event) {
        String springSessionId = event.getSessionId();
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String playerId = (String) accessor.getSessionAttributes().get("playerId");
        log.info("Spring STOMP session disconnected, springSessionId: " + springSessionId + ", playerId: " + playerId);
    }

}
