package net.verany.api.gamemode;

import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.driver.service.*;
import de.dytanic.cloudnet.ext.bridge.BridgeServiceProperty;
import de.dytanic.cloudnet.wrapper.Wrapper;
import lombok.SneakyThrows;
import net.verany.api.gamemode.server.SimplifiedServerInfo;

import java.util.*;
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
        return getOnlinePlayers(gameMode.getTargetGroup());
    }

    @Override
    public int getOnlinePlayers(String... groups) {
        return getOnlinePlayers(groups, new String[]{});
    }

    @SneakyThrows
    @Override
    public int getOnlinePlayers(String[] groups, String[] servers) {
        int i = 0;
        for (String s : groups)
            for (ServiceInfoSnapshot serviceInfoSnapshot : CloudNetDriver.getInstance().getCloudServiceProvider().getCloudServicesByGroupAsync(s).get()) {
                List<String> serversList = Arrays.asList(servers);
                if (!serversList.isEmpty() && !serversList.contains(serviceInfoSnapshot.getName())) continue;
                if (serviceInfoSnapshot.getProperty(BridgeServiceProperty.ONLINE_COUNT).isPresent())
                    i += serviceInfoSnapshot.getProperty(BridgeServiceProperty.ONLINE_COUNT).get();
            }
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

    @Override
    public void startService(AbstractGameMode gameMode, Consumer<ServiceInfoSnapshot> consumer) {
        startService(gameMode.getTargetGroup(), consumer);
    }

    @Override
    public void startService(String[] groups, Consumer<ServiceInfoSnapshot> consumer) {
        for (String group : groups) {
            ServiceTask serviceTask = CloudNetDriver.getInstance().getServiceTaskProvider().getServiceTask(group);
            ServiceInfoSnapshot serviceInfoSnapshot = CloudNetDriver.getInstance().getCloudServiceFactory().createCloudService(serviceTask);
            consumer.accept(serviceInfoSnapshot);
            serviceInfoSnapshot.provider().start();
        }
    }

    @Override
    public void startService(String group, Consumer<ServiceInfoSnapshot> consumer) {
        startService(new String[]{group}, consumer);
    }

    @Override
    public void startService(String group) {
        startService(group, simplifiedServerInfo -> {
        });
    }

    @Override
    public String getCurrentServiceName() {
        return Wrapper.getInstance().getServiceId().getName();
    }

    @Override
    public String getCurrentGroupName() {
        return Wrapper.getInstance().getServiceId().getTaskName();
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
