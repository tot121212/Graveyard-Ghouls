package com.totsnuk.graveyardghouls.controller.state;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import lombok.extern.slf4j.Slf4j;

/**
 * Basic implementation of a state machine with pre, main, and post hooks.
 *
 * @param <E> Enum type representing the states of the state machine
 */
@Slf4j
public class StateMachine<E extends Enum<E>> {

    /**
     * The current state of the state machine
     */
    private E currentState;

    /**
     * Whether to emit when setState is called
     */
    private boolean emitOnSetState = true;

    private final Map<E, List<Consumer<Object>>> preHooks = new HashMap<>();
    private final Map<E, List<Consumer<Object>>> mainHooks = new HashMap<>();
    private final Map<E, List<Consumer<Object>>> postHooks = new HashMap<>();
    private final Set<E> states = new HashSet<>();

    /**
     * Adds a new state to the state machine and initializes hooks.
     *
     * @param state the state to add
     */
    public void addState(E state) {
        if (!states.contains(state)) {
            states.add(state);
            preHooks.put(state, new ArrayList<>());
            mainHooks.put(state, new ArrayList<>());
            postHooks.put(state, new ArrayList<>());
            log.info("Registering new state: {}", state);
        }
    }

    /**
     * Sets the current state and optionally emits its event hooks.
     *
     * @param state the new current state
     * @param args  structured argument object to pass to hooks when emitting
     */
    public void setState(E state, Object args) {
        checkStateExists(state);
        currentState = state;
        if (emitOnSetState)
            emit(state, args);
    }

    /**
     * Emits an event for the current state.
     *
     * @param args structured argument object for the event hooks
     * @throws IllegalStateException if no current state is set
     */
    public void emitCurrent(Object args) {
        if (currentState == null)
            throw new IllegalStateException("No current state set");
        emit(currentState, args);
    }

    /**
     * Returns the current state of the state machine.
     *
     * @return the current state
     */
    public E getCurrentState() {
        return currentState;
    }

    /**
     * Sets whether {@link #setState(Enum, Object)} should automatically emit hooks.
     *
     * @param emitOnSetState true to automatically emit, false otherwise
     */
    public void setEmitOnSetState(boolean emitOnSetState) {
        this.emitOnSetState = emitOnSetState;
    }

    /**
     * Emits pre, main, and post hooks for a given state synchronously.
     *
     * @param state the state for which to emit hooks
     * @param args  structured argument object for the hooks
     */
    private void emit(E state, Object args) {
        log.info("Emitting event for state: {} with args: {}", state, args);
        checkStateExists(state);
        preHooks.get(state).forEach(h -> h.accept(args));
        mainHooks.get(state).forEach(h -> h.accept(args));
        postHooks.get(state).forEach(h -> h.accept(args));
    }

    /**
     * Validates that a state exists in the state machine.
     *
     * @param state the state to check
     * @throws IllegalStateException if the state is not registered
     */
    private void checkStateExists(E state) {
        if (!states.contains(state)) {
            log.error("State not registered: {}", state);
            throw new IllegalStateException("State not registered: " + state);
        }
    }

    /**
     * Retrieves the list of hooks for a state.
     *
     * @param hooks the map of hooks to query
     * @param state the state whose hooks to retrieve
     * @return the list of hooks for the state
     */
    private List<Consumer<Object>> getHooks(Map<E, List<Consumer<Object>>> hooks, E state) {
        log.debug("Retrieving hooks for state: {}", state);
        checkStateExists(state);
        return hooks.get(state);
    }

    /**
     * Registers a pre-hook for a state.
     *
     * @param state the state to attach the pre-hook
     * @param hook  the hook function to execute before the main event
     */
    public void onPre(E state, Consumer<Object> hook) {
        log.info("Registering pre hook for state: {}", state);
        getHooks(preHooks, state).add(hook);
    }

    /**
     * Registers a main hook for a state.
     *
     * @param state the state to attach the main hook
     * @param hook  the hook function to execute during the main event
     */
    public void onEvent(E state, Consumer<Object> hook) {
        log.info("Registering event hook for state: {}", state);
        getHooks(mainHooks, state).add(hook);
    }

    /**
     * Registers a post-hook for a state.
     *
     * @param state the state to attach the post-hook
     * @param hook  the hook function to execute after the main event
     */
    public void onPost(E state, Consumer<Object> hook) {
        log.info("Registering post hook for state: {}", state);
        getHooks(postHooks, state).add(hook);
    }

}
