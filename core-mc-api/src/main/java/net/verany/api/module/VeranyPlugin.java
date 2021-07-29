package net.verany.api.module;

import lombok.Getter;
import lombok.Setter;
import net.verany.api.AbstractVerany;
import net.verany.api.interfaces.IDefault;
import net.verany.api.interfaces.IMetadataInfo;
import net.verany.api.plugin.IVeranyPlugin;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public abstract class VeranyPlugin extends JavaPlugin implements Serializable, IVeranyPlugin {

    private VeranyModule module;
    private VeranyModule.DatabaseConnection connection;
    private VeranyProject project;

    public void init() {
    }

    public void setMetadata(Player player, String key, Object o) {
        player.setMetadata(key, new FixedMetadataValue(this, o));
    }

    public void removeMetadata(Player player, String key) {
        if (!player.hasMetadata(key)) return;
        player.removeMetadata(key, this);
    }

    public List<MetadataValue> getMetadata(Player player, String key) {
        if (!player.hasMetadata(key))
            return new ArrayList<>();
        return player.getMetadata(key);
    }

    public <T extends IMetadataInfo> void setMetadata(Player player, Class<T> tClass, T t) {
        setMetadata(player, tClass.getName(), t);
    }

    public <T extends IMetadataInfo> boolean hasMetadata(Player player, Class<T> tClass) {
        return player.hasMetadata(tClass.getName());
    }

    public <T extends IMetadataInfo> void removeMetadata(Player player, Class<T> tClass) {
        removeMetadata(player, tClass.getName());
    }

    public <T extends IMetadataInfo> T getMetadata(Player player, Class<T> tClass) {
        return (T) getMetadata(player, tClass.getName()).get(0).value();
    }

    @Override
    public <T extends IDefault<UUID>> T getPlayer(UUID key, Class<T> tClass) {
        return AbstractVerany.getPlayer(key, tClass);
    }

    @Override
    public <T extends IDefault<UUID>> void setPlayer(Class<T> tClass, T veranyPlayer) {
        AbstractVerany.setPlayer(tClass, veranyPlayer);
    }
}
