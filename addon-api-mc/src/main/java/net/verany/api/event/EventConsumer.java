package net.verany.api.event;

import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * @param <T> type of {@link Event Event.class}
 */
public interface EventConsumer<T extends Event> extends Listener {

    /**
     * Calls if specified event of {@code <T>} gets called
     *
     * @param event type of event
     */
    @EventHandler
    void call(T event);

}
