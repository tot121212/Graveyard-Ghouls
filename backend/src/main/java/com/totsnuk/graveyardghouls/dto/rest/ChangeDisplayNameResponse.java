package com.totsnuk.graveyardghouls.dto.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChangeDisplayNameResponse {
    private final boolean wasChanged;
}
