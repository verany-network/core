package net.verany.volcano;

import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.driver.service.ServiceInfoSnapshot;
import de.dytanic.cloudnet.driver.service.ServiceTask;
import de.dytanic.cloudnet.wrapper.Wrapper;
import net.verany.api.AbstractVerany;
import net.verany.api.gamemode.GameMode;
import net.verany.api.gamemode.GameState;
import net.verany.api.module.VeranyPlugin;
import net.verany.api.module.VeranyProject;
import net.verany.volcano.round.*;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class VeranyServer {

    public static final List<AbstractVolcanoRound> ROUNDS = new ArrayList<>();
    private static final Map<String, Document> EXTRA = new HashMap<>();

    public static IRoundLoader roundLoader;

    public static void registerGameMode(VeranyPlugin project, GameMode gameMode) {
        loadRounds(project);
        for (int i = 0; i < project.getModule().maxRounds(); i++) {
            AbstractVolcanoRound round = new VolcanoRound(project, gameMode);
            ROUNDS.add(round);
        }
    }

    public static void loadRounds(VeranyProject project) {
        roundLoader = new RoundLoader(project);
        roundLoader.load();
    }

    public static void shutdown() {
        roundLoader.update();
    }

    public static void checkPlayers(VeranyPlugin plugin) {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            int lobbyRounds = 0;

            for (AbstractVolcanoRound round : ROUNDS) {
                if (round.getSettingValue(GameSetting.GAME_STATE).equals(GameState.WAITING))
                    lobbyRounds++;

                if (lobbyRounds < round.getProject().getModule().maxRounds()) {

                }

                double percentage = round.getSettingValue(GameSetting.START_WHEN_FULL);
                if (percentage == -1) continue;
                double online = round.getPlayers().size() * 100D / round.getSettingValue(GameSetting.MAX_PLAYERS);
                if (online >= percentage && !round.isNewStarted()) {
                    round.setNewStarted(true);
                    ServiceTask serviceTask = CloudNetDriver.getInstance().getServiceTaskProvider().getServiceTask("Bingo");
                    ServiceInfoSnapshot serviceInfoSnapshot = CloudNetDriver.getInstance().getCloudServiceFactory().createCloudService(serviceTask);

                    if (serviceInfoSnapshot != null) {
                        serviceInfoSnapshot.provider().start();
                    }
                }
            }
        }, 0, 1000);
    }

    public static void setGameManager(Consumer<List<AbstractVolcanoRound>> rounds) {
        rounds.accept(ROUNDS);
    }

    public static void updateServer(Map<AbstractVolcanoRound, Document> extra) {
        List<Document> documents = new ArrayList<>();

        Document serverInfo = new Document();
        serverInfo.append("runningRounds", ROUNDS.size());
        documents.add(serverInfo);

        extra.forEach((round, document) -> {
            EXTRA.put(round.getId(), document);
        });

        for (AbstractVolcanoRound round : ROUNDS) {
            Document document = new Document("id", round.getId());

            List<UUID> players = new ArrayList<>();
            round.getPlayers().forEach(iPlayerInfo -> players.add(iPlayerInfo.getUniqueId()));

            document.append("players", players);
            document.append("max_players", round.getSettingValue(GameSetting.MAX_PLAYERS));

            if (EXTRA.containsKey(round.getId()))
                document.putAll(EXTRA.get(round.getId()));

            documents.add(document);
        }

        ServerRoundData serverRoundData = new ServerRoundData(documents);
        String data = AbstractVerany.GSON.toJson(serverRoundData);

        Wrapper.getInstance().getCurrentServiceInfoSnapshot().getProperties().append("round_data", data);
        Wrapper.getInstance().publishServiceInfoUpdate();

    }

}
