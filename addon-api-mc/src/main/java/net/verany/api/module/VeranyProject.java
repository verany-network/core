package net.verany.api.module;

import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.verany.api.AbstractVerany;
import net.verany.api.interfaces.IDefault;
import net.verany.api.loader.LoadObject;
import net.verany.api.loader.Loader;
import net.verany.api.logger.VeranyLog;
import net.verany.api.metadata.IMetadataInfo;
import net.verany.api.module.VeranyModule;
import net.verany.api.module.VeranyModule.DatabaseConnection;
import net.verany.api.player.IPlayerInfo;
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
public abstract class VeranyProject extends JavaPlugin implements Serializable, IVeranyPlugin {

    private VeranyModule module;
    private DatabaseConnection connection;
    private BukkitAudiences adventure;

    public void init() {
        adventure = BukkitAudiences.create(this);
        VeranyLog.info(getModule(), AbstractVerany.format("{0} initialized (v{1})", module.name(), module.version()));
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

    public <T extends IMetadataInfo> T getMetadata(Player player, Class<T> tClass) {
        return (T) getMetadata(player, tClass.getName()).get(0).value();
    }

    public <T extends IDefault<UUID>> T getPlayer(String key, Class<T> tClass) {
        return AbstractVerany.getPlayer(key, tClass);
    }

    @Override
    public <T extends IDefault<UUID>> T getPlayer(UUID key, Class<T> tClass) {
        return getPlayer(key.toString(), tClass);
    }

    @Override
    public <T extends LoadObject> T getData(UUID uuid, Class<T> dataClass, Class<? extends IDefault<UUID>> playerClass) {
        if (((Loader) getPlayer(uuid, playerClass)).getDataOptional(dataClass).isEmpty()) return null;
        return ((Loader) getPlayer(uuid, playerClass)).getDataOptional(dataClass).get();
    }

    @Override
    public <T extends IDefault<?>> void setPlayer(Class<T> tClass, T veranyPlayer) {
        AbstractVerany.setPlayer(tClass, veranyPlayer);
    }
}
