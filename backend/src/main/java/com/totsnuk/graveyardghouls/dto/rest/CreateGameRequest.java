package com.totsnuk.graveyardghouls.dto.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateGameRequest {
    // Name the player wants to enhabit
    private final String name;
}
