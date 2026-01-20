package poc.swt.browser.tests.app.eventbus;

import java.util.*;
import java.util.function.Consumer;

public final class EventBus<T> {
    private final Map<Class<? extends T>, Set<Consumer<T>>> listeners = Collections.synchronizedMap(new HashMap<>());

    public <E extends T> void subscribe(Class<E> eventType, Consumer<E> listener) {
        listeners.computeIfAbsent(eventType, k -> new HashSet<>()).add(object -> listener.accept(eventType.cast(object)));
    }

    public void unsubscribe(Class<? extends T> eventType, Consumer<? extends T> listener) {
        Set<Consumer<T>> eventListeners = listeners.get(eventType);
        if (eventListeners != null) {
            eventListeners.remove(listener);
            if (eventListeners.isEmpty()) {
                listeners.remove(eventType);
            }
        }
    }

    public void publish(T event) {
        Set<Consumer<T>> eventListeners = listeners.get(event.getClass());
        if (eventListeners != null) {
            eventListeners.forEach(listener -> listener.accept(event));
        }
    }
}

