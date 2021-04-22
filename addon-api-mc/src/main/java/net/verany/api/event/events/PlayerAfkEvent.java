package net.verany.api.event.events;

import lombok.Getter;
import net.verany.api.AbstractVerany;
import net.verany.api.language.EnumLanguage;
import net.verany.api.player.IPlayerInfo;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

@Getter
public class PlayerAfkEvent  extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();

    public PlayerAfkEvent(Player who) {
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
