package com.totsnuk.graveyardghouls.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;

import com.totsnuk.graveyardghouls.enums.SessionAttributes;
import com.totsnuk.graveyardghouls.pojo.GameSession;
import com.totsnuk.graveyardghouls.pojo.Participant;

import lombok.AllArgsConstructor;

/**
 * This controller handles all game interactions that are over STOMP websocket
 */
@Controller
@MessageMapping("/app/lobby")
@SendTo("/topic/lobby")
@AllArgsConstructor
public class GameLobbyController {

    @MessageMapping("/changeDisplayName/{name}")
    public Boolean msgChangePlayerDisplayName(SimpMessageHeaderAccessor sha,
            @PathVariable String name) {
        // get participant from stomp session attributes
        GameSession gameSession = (GameSession) sha.getSessionAttributes().get(SessionAttributes.GAME_SESSION);
        Participant participant = (Participant) sha.getSessionAttributes().get(SessionAttributes.PARTICIPANT);
        if (gameSession == null || participant == null)
            return false;

        //TODO: implement

        return true;
    }

    @MessageMapping("/readyUp")
    public ResponseEntity<Void> msgReadyUp() {
        boolean result = false;
        return result ? ResponseEntity.status(HttpStatus.OK).build()
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
