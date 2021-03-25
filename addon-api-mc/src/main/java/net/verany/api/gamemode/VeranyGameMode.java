package net.verany.api.gamemode;

public class VeranyGameMode {

    public static final AbstractGameMode FLAG_WARS = new GameModeWrapper("FlagWars", new String[]{"FW"}, "flagwars");
    public static final AbstractGameMode HUB = new GameModeWrapper("Hub", new String[]{"Hub"}, "hubsystem");
    public static final AbstractGameMode VIP_HUB = new GameModeWrapper("VIP-Hub", new String[]{"VIP-Hub"}, "hubsystem");
    public static final AbstractGameMode ARCADE = new GameModeWrapper("Arcade", new String[]{"Bingo"}, "bingo");
}
