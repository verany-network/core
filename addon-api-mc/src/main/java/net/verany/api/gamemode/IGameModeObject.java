package net.verany.api.gamemode;

import net.verany.api.gamemode.server.SimplifiedServerInfo;

import java.util.List;
import java.util.function.Consumer;

public interface IGameModeObject {

    String getRatingStars(AbstractGameMode gameMode);

    double getRating(AbstractGameMode gameMode);

    int getOnlinePlayers(AbstractGameMode gameMode);

    int getOnlinePlayers(AbstractGameMode gameMode, String... groups);

    int getOnlinePlayers(AbstractGameMode gameMode, String[] groups, String[] servers);

    void getOnlineServers(Consumer<List<SimplifiedServerInfo>> consumer);

}
