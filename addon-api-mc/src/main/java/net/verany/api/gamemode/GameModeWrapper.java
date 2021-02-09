package net.verany.api.gamemode;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class GameModeWrapper extends AbstractGameMode {

    public static List<AbstractGameMode> VALUES = new CopyOnWriteArrayList<>();

    public GameModeWrapper(String name, String[] targetGroups) {
        super(name, targetGroups);
        VALUES.add(this);
    }
}
