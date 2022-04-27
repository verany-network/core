package net.verany.api;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import lombok.Getter;
import net.verany.api.listener.MultiProxyListener;
import net.verany.api.module.VeranyModule;
import net.verany.api.module.VeranyPlugin;
import net.verany.api.multiproxy.IMultiProxyObject;
import net.verany.api.player.IPlayerInfo;
import net.verany.api.player.friend.IFriendObject;
import org.slf4j.Logger;

import java.util.Collection;

@Getter
@Plugin(
        id = "corexeceutor",
        name = "CoreExecutor",
        version = "2022.4.1",
        description = "A plugin",
        authors = {"tylix", "Gamingcode"}
)
@VeranyModule(
        name = "Core",
        version = "2022.4.1",
        authors = {"tylix"}
)
public class Core extends VeranyPlugin {

    public static Core INSTANCE;

    private final ProxyServer server;
    private final Logger logger;

    private IMultiProxyObject multiProxyObject;

    @Inject
    public Core(ProxyServer server, Logger logger) {
        INSTANCE = this;
        this.server = server;
        this.logger = logger;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        Verany.connectToWebsocket();
        Verany.loadModule(this, this::init);
    }

    @Override
    public void init() {

        Verany.MESSENGER.auth(this);
        Verany.reloadMessages(this);

        initListener();
    }

    private void initListener() {
        server.getEventManager().register(this, new MultiProxyListener());
    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {
        for (IPlayerInfo registeredPlayer : Verany.getPlayers(IPlayerInfo.class)) {
            registeredPlayer.getFriendObject().setStatus(IFriendObject.OFFLINE);
        }
    }

    @Override
    public Collection<Player> getAllPlayers() {
        return server.getAllPlayers();
    }
}
