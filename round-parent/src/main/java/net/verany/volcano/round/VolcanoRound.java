package net.verany.volcano.round;

import net.verany.api.gamemode.AbstractGameMode;
import net.verany.api.module.VeranyProject;

import java.util.HashMap;
import java.util.Map;

public class VolcanoRound extends AbstractVolcanoRound {

    private final Map<Class<? extends AbstractGameManager>, AbstractGameManager> gameManagerMap = new HashMap<>();

    public VolcanoRound(VeranyProject project, AbstractGameMode gameMode) {
        super(project, gameMode);
    }

    @Override
    public <T extends AbstractGameManager> T getGameManager(Class<T> tClass) {
        return (T) gameManagerMap.get(tClass);
    }

    @Override
    public <T extends AbstractGameManager> void setGameManager(Class<T> tClass, T gameManager) {
        gameManagerMap.put(tClass, gameManager);
    }
}
