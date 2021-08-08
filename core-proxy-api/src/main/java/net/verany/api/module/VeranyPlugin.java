package net.verany.api.module;

import com.velocitypowered.api.proxy.Player;
import lombok.Getter;
import lombok.Setter;
import net.verany.api.AbstractVerany;
import net.verany.api.interfaces.IDefault;
import net.verany.api.interfaces.IMetadataInfo;
import net.verany.api.plugin.IVeranyPlugin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public abstract class VeranyPlugin implements Serializable, IVeranyPlugin {

    private VeranyModule module;
    private VeranyModule.DatabaseConnection connection;
    private VeranyProject project;

    public void init() {
    }

    public abstract Collection<Player> getAllPlayers();

    @Override
    public <T extends IDefault<UUID>> T getPlayer(UUID key, Class<T> tClass) {
        return AbstractVerany.getPlayer(key, tClass);
    }

    @Override
    public <T extends IDefault<UUID>> void setPlayer(Class<T> tClass, T veranyPlayer) {
        AbstractVerany.setPlayer(tClass, veranyPlayer);
    }
}
