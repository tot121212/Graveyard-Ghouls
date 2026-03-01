package com.totsnuk.graveyardghouls.dto.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JoinGameResponse {
    private final String result;
    private final String participantId;
    private final String privateToken;
}
