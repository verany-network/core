package net.verany.api.npc.event;

import net.minecraft.server.v1_16_R3.PacketPlayInUseEntity;
import net.verany.api.npc.INPC;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class NPCInteractEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();

    private final INPC npc;
    private final PacketPlayInUseEntity.EnumEntityUseAction interactType;

    public NPCInteractEvent(Player who, INPC npc, PacketPlayInUseEntity.EnumEntityUseAction interactType) {
        super(who);
        this.npc = npc;
        this.interactType = interactType;
    }

    public INPC getNpc() {
        return npc;
    }

    public PacketPlayInUseEntity.EnumEntityUseAction getInteractType() {
        return interactType;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
