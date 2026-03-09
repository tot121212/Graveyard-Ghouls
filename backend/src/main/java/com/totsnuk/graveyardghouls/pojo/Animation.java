package com.totsnuk.graveyardghouls.pojo;

import com.totsnuk.graveyardghouls.events.AnimationEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Animation {
    private final AnimationEnum element;
    private final long ms;
}