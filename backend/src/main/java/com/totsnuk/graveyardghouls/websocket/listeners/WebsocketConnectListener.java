package com.totsnuk.graveyardghouls.websocket.listeners;

import java.util.HashMap;

import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class WebsocketConnectListener implements ApplicationListener<SessionConnectEvent> {

    @Override
    public void onApplicationEvent(SessionConnectEvent event) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());

        // Get headers sent in the CONNECT frame
        String playerPublicId = sha.getFirstNativeHeader("playerPublicId");
        String playerPrivateId = sha.getFirstNativeHeader("playerPrivateId");

        log.info("Public ID: " + playerPublicId);
        log.info("Private ID: " + playerPrivateId);

        // Set STOMP attributes
        if (sha.getSessionAttributes() == null) {
            sha.setSessionAttributes(new HashMap<>());
        }
        sha.getSessionAttributes().put("playerPublicId", playerPublicId);
        sha.getSessionAttributes().put("playerPrivateId", playerPrivateId);

        log.info("Spring STOMP session connected");
    }
}