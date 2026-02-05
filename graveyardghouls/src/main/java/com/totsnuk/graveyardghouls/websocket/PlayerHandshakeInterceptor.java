package com.totsnuk.graveyardghouls.websocket;

import java.util.Map;
import java.util.UUID;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import com.totsnuk.graveyardghouls.pojo.PlayerSession;
import com.totsnuk.graveyardghouls.service.PlayerSessionService;

import jakarta.servlet.http.Cookie;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class PlayerHandshakeInterceptor implements HandshakeInterceptor {

    private final PlayerSessionService playerSessionService;

    public PlayerHandshakeInterceptor(PlayerSessionService playerSessionService) {
        this.playerSessionService = playerSessionService;
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Map<String, Object> attributes) {
        String token = null;
        // look for session-token cookie and try to get token
        if (request instanceof ServletServerHttpRequest servletRequest) {
            Cookie[] cookies = servletRequest.getServletRequest().getCookies();
            if (cookies != null) {
                for (Cookie c : cookies) {
                    if (c.getName().equals("session-token")) {
                        token = c.getValue();
                    }
                }
            }
        }

        PlayerSession session;
        if (token != null && playerSessionService.hasSession(token)) {
            session = playerSessionService.getSession(token);
        } else {
            // No valid token so create new session
            String playerId = UUID.randomUUID().toString();
            token = playerSessionService.createSession(playerId);

            // send token back as cookie or STOMP message
            if (response instanceof ServletServerHttpResponse servletResponse) {
                Cookie cookie = new Cookie("session-token", token);
                cookie.setPath("/");
                servletResponse.getServletResponse().addCookie(cookie);
            }

            session = playerSessionService.getSession(token);
        }

        // store playerId in STOMP session attributes
        attributes.put("playerId", session.getId());
        attributes.put("token", token);
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Exception exception) {
    }
}
