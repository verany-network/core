package net.verany.api.event.events;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class PlayerLoadCompleteEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();

    public PlayerLoadCompleteEvent(Player who) {
        super(who);
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
