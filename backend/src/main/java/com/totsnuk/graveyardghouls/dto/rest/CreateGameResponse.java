package com.totsnuk.graveyardghouls.dto.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateGameResponse {
    private final String gameSessionId;
}
