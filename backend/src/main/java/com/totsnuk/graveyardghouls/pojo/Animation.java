package com.totsnuk.graveyardghouls.pojo;

import com.totsnuk.graveyardghouls.enums.AnimationEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Animation {
    private final AnimationEnum element;
    private final long ms;
}
