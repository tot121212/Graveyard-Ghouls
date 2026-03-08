package com.totsnuk.graveyardghouls.pojo;

import com.totsnuk.graveyardghouls.events.AnimationEvent;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Animation {
    public static record Payload(long ms, long timePlayed) {
    }

    private final AnimationEvent element;
    private final long ms;
}