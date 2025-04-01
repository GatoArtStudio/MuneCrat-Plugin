package com.github.gatoartstudios.munecraft.core.event;

import com.github.gatoartstudios.munecraft.core.enums.EventType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class Event {

    /**
     * Instance of the event class.
     */
    private static final Event instance = new Event();
    /**
     * Map containing all event listeners.
     */
    private final Map<EventType, List<Consumer<Object[]>>> listeners = new HashMap<>();

    /**
     * Constructor for the event class.
     */
    public Event() {}

    /**
     * Gets the instance of the event class.
     * @return The instance of the event class.
     */
    public static Event getInstance() {
        return instance;
    }

    /**
     * Adds a new listener to the event system.
     * @param eventType The event to listen to.
     * @param function The function to execute when the event is emitted.
     */
    public void addListener(EventType eventType, Consumer<Object[]> function) {
        listeners.computeIfAbsent(eventType, k -> new ArrayList<>()).add(function);
    }

    /**
     * Removes a listener from the event system.
     * @param eventType The event to remove the listener from.
     * @param function The function to remove from the event.
     */
    public void removeListener(EventType eventType, Consumer<Object[]> function) {
        List<Consumer<Object[]>> eventListeners = listeners.get(eventType);
        if (eventListeners != null) {
            eventListeners.remove(function);
            if (eventListeners.isEmpty()) {
                listeners.remove(eventType);
            }
        }
    }

    /**
     * Emits an event to all listeners.
     * @param eventType The event to emit.
     * @param args The arguments to pass to the listeners.
     */
    public void emit(EventType eventType, Object... args) {
        if (listeners.containsKey(eventType)) {
            for (Consumer<Object[]> function : listeners.get(eventType)) {
                function.accept(args);
            }
        }
    }
}
