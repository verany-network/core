package net.verany.api.player;

import net.verany.api.interfaces.ILoadObject;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public interface IPlayerLoader extends ILoadObject<UUID> {

    default Player player() {
        return Bukkit.getPlayer(getKey());
    }

}
