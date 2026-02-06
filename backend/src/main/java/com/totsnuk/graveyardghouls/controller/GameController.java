package com.totsnuk.graveyardghouls.controller;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

import com.totsnuk.graveyardghouls.component.GameState;
import com.totsnuk.graveyardghouls.pojo.Action;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class GameController {
    // when user sends a specific type of post it is added to the actionQueue
    private final BlockingQueue<Action> actionQueue = new LinkedBlockingQueue<>();
    private GameState gameState;

    @MessageMapping("/join")
    public String postJoin(@RequestBody String entity) {
        // join the first active gameSession that exists
        return entity;
    }
}
