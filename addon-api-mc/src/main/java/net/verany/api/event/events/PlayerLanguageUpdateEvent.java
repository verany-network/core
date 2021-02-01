package net.verany.api.event.events;

import lombok.Getter;
import net.verany.api.AbstractVerany;
import net.verany.api.language.EnumLanguage;
import net.verany.api.player.IPlayerInfo;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

@Getter
public class PlayerLanguageUpdateEvent extends PlayerEvent {

    private final EnumLanguage oldLanguage;
    private final EnumLanguage newLanguage;
    private final IPlayerInfo playerInfo;

    private static final HandlerList handlers = new HandlerList();

    public PlayerLanguageUpdateEvent(EnumLanguage oldLanguage, EnumLanguage newLanguage, Player who) {
        super(who);
        this.oldLanguage = oldLanguage;
        this.newLanguage = newLanguage;
        playerInfo = AbstractVerany.PROFILE_OBJECT.getPlayer(who.getUniqueId()).get();
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
