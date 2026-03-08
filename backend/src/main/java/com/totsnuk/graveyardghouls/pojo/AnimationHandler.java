package com.totsnuk.graveyardghouls.pojo;

import java.time.Instant;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.totsnuk.graveyardghouls.events.Event;
import com.totsnuk.graveyardghouls.events.EventDispatcher;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * Sends animations to the client(s) <br>
 * Ensures client cannot send inputs whilst animation is occuring
 */
@Getter
@Setter
@RequiredArgsConstructor
public class AnimationHandler {
    private final EventDispatcher<Event> eventBus;
    // private final SimpMessagingTemplate msgTemplate;
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
     * Animations will loop until none are left
     * - will be triggered again by add
     * 
     * @return If an animation was started
     */
    private void animate() {
        Animation next = queue.poll();
        if (next == null) {
            current = null;
            return;
        }
        current = next;
        long ms = current.getMs();
        long now = Instant.now().toEpochMilli();
        scheduler.schedule(this::animate, ms, TimeUnit.MILLISECONDS);
        eventBus.emit(next.getElement(), new Animation.Payload(ms, now));
    }
}
