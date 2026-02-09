package com.totsnuk.graveyardghouls.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.totsnuk.graveyardghouls.dto.GetTotalSessionsResponse;
import com.totsnuk.graveyardghouls.enums.Endpoint;
import com.totsnuk.graveyardghouls.service.GameSessionService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(Endpoint.API)
@AllArgsConstructor
@Slf4j
public class HomeController {
    public GameSessionService gameSessionService;

    @GetMapping(Endpoint.GET_TOTAL_SESSIONS)
    public ResponseEntity<GetTotalSessionsResponse> getTotalSessions() {
        log.info("get for total sessions");
        int total = gameSessionService.getSessionMapSize();
        GetTotalSessionsResponse response = new GetTotalSessionsResponse(total);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PostMapping(Endpoint.GET_GAME_SESSION_PAGE)
    public String postMethodName(@RequestBody String entity) {
        // TODO: process POST request

        return entity;
    }

}