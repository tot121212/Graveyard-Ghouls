package com.totsnuk.graveyardghouls.event;

import org.springframework.stereotype.Component;

import com.totsnuk.graveyardghouls.enums.AnimationEvent;

/**
 * Bean version of EventDispatcher
 */
@Component
public class AnimationBus extends EventDispatcher<AnimationEvent> {
}