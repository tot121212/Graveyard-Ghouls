package com.totsnuk.graveyardghouls.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.totsnuk.graveyardghouls.dto.JoinDto;
import com.totsnuk.graveyardghouls.dto.rest.CreateGameResponse;
import com.totsnuk.graveyardghouls.dto.rest.JoinGameResponse;
import com.totsnuk.graveyardghouls.enums.HTTPMode;
import com.totsnuk.graveyardghouls.enums.result.JoinResult;
import com.totsnuk.graveyardghouls.service.GameSessionService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * This controller handles all gameSession creation, deletion, joining, and
 * leaving. Also, handles
 * changing display name for players
 */
@RestController
@RequestMapping("/api/gameSession")
@AllArgsConstructor
@Slf4j
public class GameSessionController {
    private final GameSessionService gameSessionService;

    /**
     * One day in seconds
     */
    private static final int ONE_DAY = 60 * 60 * 24;

    @PostMapping("/create")
    public ResponseEntity<CreateGameResponse> postCreateGame(HttpServletResponse response) {
        String gid = gameSessionService.create();

        Cookie cookie = new Cookie("gameSessionId", gid);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // NOTE: change to true when using HTTPS
        cookie.setPath("/"); // sent for all paths on this domain
        cookie.setMaxAge(ONE_DAY);
        response.addCookie(cookie);

        return ResponseEntity.status(HttpStatus.OK).build();
        // client will then automatically do a join rest request
    }

    @PostMapping("/join/{gameSessionId}")
    public ResponseEntity<JoinGameResponse> postJoinGame(
            @PathVariable("gameSessionId") String gid,
            @CookieValue(value = "participantId", required = false) String participantId,
            @CookieValue(value = "privateToken", required = false) String privateToken,
            HttpServletResponse httpServletResponse) {

        JoinDto dto = (participantId == null || privateToken == null)
                ? gameSessionService.join(gid)
                : gameSessionService.join(gid, participantId, privateToken);

        if (dto == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        HttpStatus status = switch (dto.result()){
            case SUCCESS -> HttpStatus.OK;
            default -> HttpStatus.BAD_REQUEST;
        }

        switch (dto.result()) {
            case JoinResult.SUCCESS -> {
                if (dto.privateToken() == null || dto.participantId() == null)
                    throw new IllegalStateException("privateToken and participantId must exist");

                // add session token to http cookie
                Cookie prvCookie = new Cookie("privateToken", dto.privateToken());
                prvCookie.setHttpOnly(true);
                prvCookie.setSecure(HTTPMode.USE_SECURE_PROTOCOL);
                prvCookie.setPath("/"); // sent for all paths on this domain
                prvCookie.setMaxAge(ONE_DAY);
                httpServletResponse.addCookie(prvCookie);

                Cookie pubCookie = new Cookie("participantId", dto.participantId());
                pubCookie.setSecure(HTTPMode.USE_SECURE_PROTOCOL);
                pubCookie.setPath("/"); // sent for all paths on this domain
                pubCookie.setMaxAge(ONE_DAY);
                httpServletResponse.addCookie(pubCookie);
            }
        }
        
        return ResponseEntity.status(status).body(dto);
    }

    @PostMapping("/leave/{gameSessionId}")
    public ResponseEntity<Void> postLeaveGame(@CookieValue("playerId") String pid,
            @PathVariable("gameSessionId") String gid) {

        boolean result = gameSessionService.leave(gid, pid);

        return result ? ResponseEntity.status(HttpStatus.OK).build()
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
