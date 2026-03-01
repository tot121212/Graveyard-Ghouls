package com.totsnuk.graveyardghouls.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import lombok.AllArgsConstructor;

@Controller
@MessageMapping("/app/game")
@SendTo("/topic/lobby")
@AllArgsConstructor
public class GameActionController {

}
