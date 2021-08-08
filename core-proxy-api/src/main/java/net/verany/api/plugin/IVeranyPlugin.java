package net.verany.api.plugin;

import com.velocitypowered.api.proxy.Player;
import net.verany.api.interfaces.IDefault;
import net.verany.api.interfaces.IMetadataInfo;
import net.verany.api.module.VeranyProject;

import java.util.List;
import java.util.UUID;

public interface IVeranyPlugin extends VeranyProject {

    <T extends IDefault<UUID>> T getPlayer(UUID key, Class<T> tClass);

    <T extends IDefault<UUID>> void setPlayer(Class<T> tClass, T veranyPlayer);

}
