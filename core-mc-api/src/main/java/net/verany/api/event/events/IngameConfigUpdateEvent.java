package net.verany.api.event.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.verany.api.config.AbstractIngameConfig;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
@AllArgsConstructor
public class IngameConfigUpdateEvent extends Event {

    private final HandlerList handlerList = new HandlerList();

    private final AbstractIngameConfig<?> config;

    public HandlerList getHandlers() {
        return handlerList;
    }
}
