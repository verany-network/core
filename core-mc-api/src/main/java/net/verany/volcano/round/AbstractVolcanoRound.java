package net.verany.volcano.round;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.verany.api.AbstractVerany;
import net.verany.api.Verany;
import net.verany.api.gamemode.GameMode;
import net.verany.api.module.VeranyPlugin;
import net.verany.api.player.IPlayerInfo;
import net.verany.api.settings.AbstractSettingLoader;
import net.verany.volcano.player.IVolcanoPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@RequiredArgsConstructor
public abstract class AbstractVolcanoRound extends AbstractSettingLoader {

    private final String id = AbstractVerany.generateString(10);
    private final VeranyPlugin project;
    private final GameMode gameMode;
    private final List<IVolcanoPlayer> players = new ArrayList<>();
    private final List<IVolcanoPlayer> allPlayers = new ArrayList<>();
    private boolean newStarted = false;

    public abstract <T extends AbstractGameManager> T getGameManager(Class<T> tClass);

    public abstract <T extends AbstractGameManager> void setGameManager(Class<T> tClass, T gameManager);

    public List<IVolcanoPlayer> getOtherPlayers() {
        List<IVolcanoPlayer> toReturn = new ArrayList<>();
        for (IVolcanoPlayer player : AbstractVerany.getPlayers(IVolcanoPlayer.class))
            if (!players.contains(player))
                toReturn.add(player);
        return toReturn;
    }

    public List<IPlayerInfo> getVeranyPlayers() {
        return players.stream().map(iVolcanoPlayer -> Verany.getPlayer(iVolcanoPlayer.getUniqueId())).collect(Collectors.toList());
    }

    public List<Player> getBukkitPlayers() {
        List<Player> toReturn = new ArrayList<>();
        for (IVolcanoPlayer player : players)
            toReturn.add(Bukkit.getPlayer(player.getUniqueId()));
        return toReturn;
    }

}
