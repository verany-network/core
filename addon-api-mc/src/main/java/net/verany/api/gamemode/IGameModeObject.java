package net.verany.api.gamemode;

import de.dytanic.cloudnet.driver.service.ServiceInfoSnapshot;
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

    void startService(AbstractGameMode gameMode,  Consumer<ServiceInfoSnapshot> consumer);

    void startService(String[] groups, Consumer<ServiceInfoSnapshot> consumer);

    void startService(String group, Consumer<ServiceInfoSnapshot> consumer);

    void startService(String group);

    String getCurrentServiceName();

    String getCurrentGroupName();

}
