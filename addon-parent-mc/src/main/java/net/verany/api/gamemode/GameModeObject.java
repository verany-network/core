package net.verany.api.gamemode;

import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.driver.service.ServiceInfoSnapshot;
import de.dytanic.cloudnet.ext.bridge.BridgeServiceProperty;
import net.verany.api.gamemode.server.SimplifiedServerInfo;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class GameModeObject implements IGameModeObject {

    @Override
    public String getRatingStars(AbstractGameMode gameMode) {
        StringBuilder stringBuilder = new StringBuilder();
        double rating = getRating(gameMode);
        for (double d = 0; d < Math.round(rating); d++)
            stringBuilder.append("★");
        for (double d = 0; d < 5 - Math.round(rating); d++)
            stringBuilder.append("☆");
        return stringBuilder.toString();
    }

    @Override
    public double getRating(AbstractGameMode gameMode) {
        return 4.3;
    }

    @Override
    public int getOnlinePlayers(AbstractGameMode gameMode) {
        return getOnlinePlayers(gameMode, gameMode.getTargetGroup());
    }

    @Override
    public int getOnlinePlayers(AbstractGameMode gameMode, String... groups) {
        int i = 0;
        for (String s : groups)
            for (ServiceInfoSnapshot serviceInfoSnapshot : CloudNetDriver.getInstance().getCloudServiceProvider().getCloudServicesByGroup(s))
                i += serviceInfoSnapshot.getProperty(BridgeServiceProperty.ONLINE_COUNT).get();
        return i;
    }

    @Override
    public void getOnlineServers(Consumer<List<SimplifiedServerInfo>> consumer) {
        CloudNetDriver.getInstance().getCloudServiceProvider().getCloudServicesAsync()
                .onComplete((collectionITask, serviceInfoSnapshots) -> consumer.accept(
                        serviceInfoSnapshots.stream()
                                .map(this::createServerInfo)
                                .filter(Objects::isNull)
                                .collect(Collectors.toList())
                ))
                .onFailure(throwable -> consumer.accept(Collections.emptyList()))
                .onCancelled(task -> consumer.accept(Collections.emptyList()));
    }

    private SimplifiedServerInfo createServerInfo(ServiceInfoSnapshot serviceInfoSnapshot) {
        return serviceInfoSnapshot.getProperty(BridgeServiceProperty.IS_ONLINE).orElse(false) ? new SimplifiedServerInfo(
                serviceInfoSnapshot.getServiceId().getTaskName(),
                serviceInfoSnapshot.getServiceId().getName(),
                serviceInfoSnapshot.getServiceId().getTaskServiceId(),
                serviceInfoSnapshot.getServiceId().getNodeUniqueId(),
                serviceInfoSnapshot.getProperty(BridgeServiceProperty.ONLINE_COUNT).orElse(0)
        ) : null;

    }

}
