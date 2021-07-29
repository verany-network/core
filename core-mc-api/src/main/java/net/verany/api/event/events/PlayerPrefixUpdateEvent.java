package net.verany.api.event.events;

import lombok.Getter;
import net.verany.api.AbstractVerany;
import net.verany.api.player.IPlayerInfo;
import net.verany.api.prefix.AbstractPrefixPattern;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

@Getter
public class PlayerPrefixUpdateEvent extends PlayerEvent {

    private final AbstractPrefixPattern newPattern;
    private final IPlayerInfo playerInfo;

    private static final HandlerList handlers = new HandlerList();

    public PlayerPrefixUpdateEvent(AbstractPrefixPattern newPattern, Player who) {
        super(who);
        this.newPattern = newPattern;
        playerInfo = AbstractVerany.PROFILE_OBJECT.getPlayer(who.getUniqueId(), IPlayerInfo.class).get();
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
