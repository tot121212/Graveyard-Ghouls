package com.totsnuk.graveyardghouls.websocket;

import java.util.Map;
import java.util.Set;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import com.totsnuk.graveyardghouls.pojo.PlayerSession;
import com.totsnuk.graveyardghouls.service.PlayerSessionService;

import jakarta.servlet.http.Cookie;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@Component
@AllArgsConstructor
public class ClientHandshakeInterceptor implements HandshakeInterceptor {
    private final PlayerSessionService playerSessionService;

    /**
     * Safely retrieves PlayerSession with inputs
     * 
     * @return PlayerSession || null
     */
    private PlayerSession getPlayerSessionFromServiceSafely(String publicId, String privateId) {
        if (publicId == null && privateId == null) {
            log.warn("publicId or privateId were null");
            return null;
        }
        Set<PlayerSession> sessions = playerSessionService.getAllSessions();
        ObjectMapper mapper = new ObjectMapper();
        try {
            String json = mapper.writeValueAsString(sessions);
            log.info("Finding publicId {} and privateId {} in {}", publicId, privateId, json);
        } catch (RuntimeException e) {
            log.error("Failed to serialize sessions", e);
        }

        PlayerSession ps = playerSessionService.getSession(publicId);
        if (ps == null) {
            log.warn("Session does not exist");
            return null;
        }
        if (!privateId.equals(ps.getPrivateId())) {
            log.warn("privateId comparison: do not match");
            return null;
        }
        return ps;
    }

    /**
     * Sends playerPrivateId and playerPublicId to client via cookies
     * 
     * @param response
     * @param session
     */
    private void sendPlayerCookies(ServerHttpResponse response, PlayerSession session) {
        if (response instanceof ServletServerHttpResponse servletResponse) {
            Cookie privateCookie = new Cookie("playerPrivateId", session.getPrivateId());
            Cookie publicCookie = new Cookie("playerPublicId", session.getId());
            privateCookie.setPath("/");
            publicCookie.setPath("/");
            servletResponse.getServletResponse().addCookie(privateCookie);
            servletResponse.getServletResponse().addCookie(publicCookie);
        }
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Map<String, Object> attributes) {

        PlayerSession session = getPlayerSessionFromServiceSafely(
                request.getHeaders().getFirst("playerPublicId"),
                request.getHeaders().getFirst("playerPrivateId"));
        if (session == null) {
            // No valid id so create new session
            session = playerSessionService.createSession();

            // send cookies back
            sendPlayerCookies(response, session);
        }

        // store publicId in STOMP session attributes
        attributes.put("playerPublicId", session.getId());
        attributes.put("playerPrivateId", session.getPrivateId());
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Exception exception) {
    }
}
