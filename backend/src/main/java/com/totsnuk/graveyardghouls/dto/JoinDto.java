package com.totsnuk.graveyardghouls.dto;

import com.totsnuk.graveyardghouls.enums.result.JoinResult;

public record JoinDto(
    JoinResult result,
    String participantId,
    String privateToken
) {}
