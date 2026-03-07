package com.totsnuk.graveyardghouls.pojo;

import java.time.Instant;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.messaging.simp.SimpMessagingTemplate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Sends animations to the client(s) <br>
 * Ensures client cannot send inputs whilst animation is occuring
 */
@Getter
@Setter
@AllArgsConstructor
public class AnimationHandler {
    private final SimpMessagingTemplate msgTemplate;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final Queue<Animation> queue = new ConcurrentLinkedQueue<>();
    private Animation current;
    private boolean animating = false;

    public void add(Animation anim) {
        if (animating == true) {
            queue.add(anim);
        } else {
            animating = true;
            animate(anim);
        }
    }

    public void onFinishAnimation() {
        Animation head = queue.poll();
        animate(head);
        if (queue.isEmpty())
            animating = false;
    }

    public void animate(Animation anim) {
        long ms = anim.getMs();
        long now = Instant.now().toEpochMilli();
        scheduler.schedule(this::onFinishAnimation, ms, TimeUnit.MILLISECONDS);
        // TODO: Emit animation event to event bus
    }
}
