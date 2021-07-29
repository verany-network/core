package net.verany.api.event.events;

import lombok.Getter;
import net.verany.api.AbstractVerany;
import net.verany.api.language.EnumLanguage;
import net.verany.api.player.IPlayerInfo;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

@Getter
public class PlayerLanguageUpdateEvent extends PlayerEvent {

    private final IPlayerInfo playerInfo;
    @Deprecated
    private EnumLanguage oldLanguage;
    @Deprecated
    private EnumLanguage newLanguage;

    private static final HandlerList handlers = new HandlerList();

    public PlayerLanguageUpdateEvent(Player who) {
        super(who);
        playerInfo = AbstractVerany.PROFILE_OBJECT.getPlayer(who.getUniqueId(), IPlayerInfo.class).get();
    }

    public PlayerLanguageUpdateEvent(EnumLanguage oldLanguage, EnumLanguage newLanguage,@NotNull Player who) {
        super(who);
        this.oldLanguage = oldLanguage;
        this.newLanguage = newLanguage;
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
