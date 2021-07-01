package net.verany.volcano.event;

import lombok.Getter;
import net.verany.volcano.round.AbstractVolcanoRound;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

@Getter
public class VolcanoPlayerJoinEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();

    private final AbstractVolcanoRound round;

    public VolcanoPlayerJoinEvent(Player who, AbstractVolcanoRound round) {
        super(who);
        this.round = round;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
