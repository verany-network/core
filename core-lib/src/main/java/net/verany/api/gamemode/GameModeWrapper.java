package net.verany.api.gamemode;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class GameModeWrapper extends GameMode {

    public static List<GameMode> VALUES = new CopyOnWriteArrayList<>();

    public GameModeWrapper(String name, String[] targetGroups, String... databaseNames) {
        super(name, targetGroups, databaseNames);
        VALUES.add(this);
    }

    public static GameMode getGameModeByName(String name) {
        return VALUES.stream().filter(gameMode -> gameMode.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

}
