package net.verany.api.gamemode;

public class VeranyGameMode {

    public static final AbstractGameMode FLAG_WARS = new GameModeWrapper("FlagWars", new String[]{"FW"});
    public static final AbstractGameMode HUB = new GameModeWrapper("Hub", new String[]{"Hub"});
    public static final AbstractGameMode VIP_HUB = new GameModeWrapper("VIP-Hub", new String[]{"VIP-Hub"});
    public static final AbstractGameMode ARCADE = new GameModeWrapper("Arcade", new String[]{"Bingo"});

    public static AbstractGameMode getGameModeByName(String name) {
        for (AbstractGameMode value : GameModeWrapper.VALUES)
            if(value.getName().equalsIgnoreCase(name))
                return value;
        return null;
    }

}
