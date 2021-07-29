package net.verany.api.plugin;

import net.verany.api.interfaces.IDefault;
import net.verany.api.interfaces.IMetadataInfo;
import net.verany.api.module.VeranyProject;
import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;

import java.util.List;
import java.util.UUID;

public interface IVeranyPlugin extends VeranyProject {

    void removeMetadata(Player player, String key);

    List<MetadataValue> getMetadata(Player player, String key);

    void setMetadata(Player player, String key, Object o);

    <T extends IMetadataInfo> void setMetadata(Player player, Class<T> tClass, T t);

    <T extends IMetadataInfo> T getMetadata(Player player, Class<T> tClass);

    <T extends IDefault<UUID>> T getPlayer(UUID key, Class<T> tClass);

    <T extends IDefault<UUID>> void setPlayer(Class<T> tClass, T veranyPlayer);

}
