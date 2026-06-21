package com.totsnuk.graveyardghouls.dto.rest;
public record JoinGameResponse(
    String result,
    String participantId,
    String privateToken) {
}
