package net.verany.api.gamemode;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class GameModeWrapper extends AbstractGameMode {

    public static List<AbstractGameMode> VALUES = new CopyOnWriteArrayList<>();

    public GameModeWrapper(String name, String[] targetGroups, String... databaseNames) {
        super(name, targetGroups, databaseNames);
        VALUES.add(this);
    }

    public static AbstractGameMode getGameModeByName(String name) {
        return VALUES.stream().filter(gameMode -> gameMode.getName().equals(name)).findFirst().orElse(null);
    }

}
