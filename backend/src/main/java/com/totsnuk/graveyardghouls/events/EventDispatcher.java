package com.totsnuk.graveyardghouls.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import lombok.extern.slf4j.Slf4j;

/**
 * Basic implementation of a event dispatcher with pre, main, and post hooks.
 * - all my homies hate type safety
 *
 * @param <E> Any enum element
 */
@Slf4j
public class EventDispatcher<E extends Event> {

    private final Map<E, List<Consumer<Record>>> preHooks = new HashMap<>();
    private final Map<E, List<Consumer<Record>>> mainHooks = new HashMap<>();
    private final Map<E, List<Consumer<Record>>> postHooks = new HashMap<>();
    private final Set<E> events = new HashSet<>();

    /**
     * Adds a new event to the event dispatcher and initializes hooks.
     *
     * @param event the event to add
     */
    public void add(E event) {
        if (!events.contains(event)) {
            events.add(event);
            preHooks.put(event, new ArrayList<>());
            mainHooks.put(event, new ArrayList<>());
            postHooks.put(event, new ArrayList<>());
            log.info("Registering new event: {}", event);
        }
    }

    /**
     * Emits event hooks.
     *
     * @param event the new current event
     * @param args  structured argument object to pass to hooks when emitting
     */
    public void emit(E event, Record args) {
        exists(event);
        emitInternal(event, args);
    }

    /**
     * Emits pre, main, and post hooks for a given event synchronously.
     *
     * @param event the event for which to emit hooks
     * @param args  structured argument object for the hooks
     */
    private void emitInternal(E event, Record args) {
        log.info("Emitting event for event: {} with args: {}", event, args);
        exists(event);
        preHooks.get(event).forEach(h -> h.accept(args));
        mainHooks.get(event).forEach(h -> h.accept(args));
        postHooks.get(event).forEach(h -> h.accept(args));
    }

    /**
     * Validates that a event exists in the event dispatcher.
     *
     * @param event the event to check
     * @throws IllegalStateException if the event is not registered
     */
    private void exists(E event) {
        if (!events.contains(event)) {
            log.error("State not registered: {}", event);
            throw new IllegalStateException("State not registered: " + event);
        }
    }

    /**
     * Retrieves the list of hooks for a event.
     *
     * @param hooks the map of hooks to query
     * @param event the event whose hooks to retrieve
     * @return the list of hooks for the event
     */
    private List<Consumer<Record>> getHooks(Map<E, List<Consumer<Record>>> hooks, E event) {
        log.debug("Retrieving hooks for event: {}", event);
        exists(event);
        return hooks.get(event);
    }

    /**
     * Registers a pre-hook for a event.
     *
     * @param event the event to attach the pre-hook
     * @param hook  the hook function to execute before the main event
     */
    public void onPre(E event, Consumer<Record> hook) {
        log.info("Registering pre hook for event: {}", event);
        getHooks(preHooks, event).add(hook);
    }

    /**
     * Registers a main hook for a event.
     *
     * @param event the event to attach the main hook
     * @param hook  the hook function to execute during the main event
     */
    public void onEvent(E event, Consumer<Record> hook) {
        log.info("Registering event hook for event: {}", event);
        getHooks(mainHooks, event).add(hook);
    }

    /**
     * Registers a post-hook for a event.
     *
     * @param event the event to attach the post-hook
     * @param hook  the hook function to execute after the main event
     */
    public void onPost(E event, Consumer<Record> hook) {
        log.info("Registering post hook for event: {}", event);
        getHooks(postHooks, event).add(hook);
    }

}
