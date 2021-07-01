package net.verany.api.npc.event;

import lombok.Getter;
import net.verany.api.npc.INPC;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

@Getter
public class NPCInteractEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();

    private final INPC npc;

    public NPCInteractEvent(Player who, INPC npc) {
        super(who);
        this.npc = npc;
    }


    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
