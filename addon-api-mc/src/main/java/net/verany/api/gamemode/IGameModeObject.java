package net.verany.api.gamemode;

public interface IGameModeObject {

    String getRatingStars(AbstractGameMode gameMode);

    double getRating(AbstractGameMode gameMode);

    int getOnlinePlayers(AbstractGameMode gameMode);

    int getOnlinePlayers(AbstractGameMode gameMode, String... groups);

}
