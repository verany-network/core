package net.verany.api.npc.reader;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.PacketPlayInUseEntity;
import net.verany.api.Verany;
import net.verany.api.module.VeranyProject;
import net.verany.api.npc.INPC;
import net.verany.api.npc.event.NPCInteractEvent;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.List;

public class PacketReader {

    private final Player player;
    private final VeranyProject project;
    private Channel channel;

    public PacketReader(Player player, VeranyProject project) {
        this.player = player;
        this.project = project;
    }

    public void inject() {
        CraftPlayer cPlayer = (CraftPlayer) this.player;
        channel = cPlayer.getHandle().networkManager.k;
        channel.pipeline().addAfter("decoder", "PacketInjector", new MessageToMessageDecoder<Packet<?>>() {
            @Override
            protected void decode(ChannelHandlerContext arg0, Packet<?> packet, List<Object> arg2) throws Exception {
                arg2.add(packet);
                readPacket(packet);
            }
        });
    }

    public void uninject() {
        if (channel.pipeline().get("PacketInjector") != null) {
            channel.pipeline().remove("PacketInjector");
        }
    }


    public void readPacket(Packet<?> packet) {
        if (packet.getClass().getSimpleName().equalsIgnoreCase("PacketPlayInUseEntity")) {
            int id = (Integer) getValue(packet, "a");

            if (getValue(packet, "action").toString().equalsIgnoreCase("ATTACK") || getValue(packet, "action").toString().equalsIgnoreCase("INTERACT") || getValue(packet, "action").toString().equalsIgnoreCase("INTERACT_AT"))
                for (INPC npc : Verany.NPCS)
                    if (npc.getId() == id) {
                        System.out.println("action=" + getValue(packet, "action"));
                        Bukkit.getScheduler().scheduleSyncDelayedTask(project, () -> Bukkit.getPluginManager().callEvent(new NPCInteractEvent(player, npc)));
                    }
        }
    }


    public void setValue(Object obj, String name, Object value) {
        try {
            Field field = obj.getClass().getDeclaredField(name);
            field.setAccessible(true);
            field.set(obj, value);
        } catch (Exception e) {
        }
    }

    public Object getValue(Object obj, String name) {
        try {
            Field field = obj.getClass().getDeclaredField(name);
            field.setAccessible(true);
            return field.get(obj);
        } catch (Exception e) {
        }
        return null;
    }

}
