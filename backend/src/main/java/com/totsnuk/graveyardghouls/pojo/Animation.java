package com.totsnuk.graveyardghouls.pojo;

import com.totsnuk.graveyardghouls.enums.AnimationEvent;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Animation {
    private final AnimationEvent element;
    private final long ms;
}
