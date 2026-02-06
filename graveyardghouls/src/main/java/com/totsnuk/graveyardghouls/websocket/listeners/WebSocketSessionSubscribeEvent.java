package com.totsnuk.graveyardghouls.websocket.listeners;

import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@AllArgsConstructor
public class WebSocketSessionSubscribeEvent implements ApplicationListener<SessionSubscribeEvent> {

    private final SimpMessagingTemplate msg;

    @Override
    public void onApplicationEvent(SessionSubscribeEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String playerId = (String) accessor.getSessionAttributes().get("playerId");
        log.info("Spring STOMP session subscribed: " + "playerId: " + playerId);
        // send user their player session token
        msg.convertAndSend("/topic/static", "Server: Subscribe established");
    }

}
