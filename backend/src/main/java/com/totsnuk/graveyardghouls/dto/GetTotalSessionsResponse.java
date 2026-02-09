package com.totsnuk.graveyardghouls.dto;

import lombok.Getter;

@Getter
public class GetTotalSessionsResponse {
    private final int total;

    public GetTotalSessionsResponse(int total) {
        this.total = total;
    }
}