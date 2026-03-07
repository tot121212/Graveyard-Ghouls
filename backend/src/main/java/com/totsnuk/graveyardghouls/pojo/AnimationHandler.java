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

    /**
     * Adds animation to queue and triggers animate if not already triggered
     */
    public boolean add(Animation anim) {
        if (anim == null)
            return false;
        queue.add(anim);
        if (current == null)
            animate();
        return true;
    }

    /**
     * @return If an animation was started
     */
    private void animate() {
        Animation next = queue.poll();
        if (next == null) {
            current = null;
        }
        current = next;
        long ms = current.getMs();
        long now = Instant.now().toEpochMilli();
        scheduler.schedule(this::animate, ms, TimeUnit.MILLISECONDS);

        // TODO: Emit animation event to event bus
        // with `now` for proper timing on client
    }
}
