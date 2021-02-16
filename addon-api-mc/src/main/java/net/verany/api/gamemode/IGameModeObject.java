package net.verany.api.gamemode;

import net.verany.api.gamemode.server.SimplifiedServerInfo;

import java.util.List;
import java.util.function.Consumer;

public interface IGameModeObject {

    String getRatingStars(AbstractGameMode gameMode);

    double getRating(AbstractGameMode gameMode);

    int getOnlinePlayers(AbstractGameMode gameMode);

    int getOnlinePlayers(String... groups);

    int getOnlinePlayers(String[] groups, String[] servers);

    void getOnlineServers(Consumer<List<SimplifiedServerInfo>> consumer);

    void startService(AbstractGameMode gameMode,  Consumer<SimplifiedServerInfo> consumer);

    void startService(String[] groups, Consumer<SimplifiedServerInfo> consumer);

    void startService(String group, Consumer<SimplifiedServerInfo> consumer);

    void startService(String group);

    String getCurrentServiceName();

    String getCurrentGroupName();

}
