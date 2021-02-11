package net.verany.api.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.verany.api.game.GameState;

import java.util.concurrent.TimeUnit;

public class IngameConfig<T> extends AbstractIngameConfig<T> {

    public static final AbstractIngameConfig<Boolean> CHAT = new IngameConfig<>(Boolean.class, true);
    public static final AbstractIngameConfig<String> CHAT_FORMAT = new IngameConfig<>(String.class, "{0}{2}§8: §f{3}");
    public static final AbstractIngameConfig<Boolean> TAB_LIST = new IngameConfig<>(Boolean.class, true);
    public static final AbstractIngameConfig<Boolean> TAB_LIST_CLAN = new IngameConfig<>(Boolean.class, false);
    public static final AbstractIngameConfig<String> TAB_LIST_FORMAT = new IngameConfig<>(String.class, "{0}");
    public static final AbstractIngameConfig<Boolean> PLAYER_COLLISION = new IngameConfig<>(Boolean.class, true);
    public static final AbstractIngameConfig<Long> AFK_TIME = new IngameConfig<>(Long.class, TimeUnit.MINUTES.toMillis(5));
    public static final AbstractIngameConfig<Boolean> AFK = new IngameConfig<>(Boolean.class, false);
    public static final AbstractIngameConfig<GameState> GAME_STATE = new IngameConfig<>(GameState.class, GameState.LOBBY);

    public IngameConfig(Class<T> tClass, T defaultValue) {
        super(tClass, defaultValue);
    }

}
