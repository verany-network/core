package net.verany.api.gamemode;

public class VeranyGameMode {

    public static final GameMode FLAG_WARS = new GameModeWrapper("FlagWars", new String[]{"FW-Hub", "FW-2x1"}, "flagwars");
    public static final GameMode HUB = new GameModeWrapper("Hub", new String[]{"Hub"}, "hubsystem");
    public static final GameMode VIP_HUB = new GameModeWrapper("VIP-Hub", new String[]{"VIP-Hub"}, "hubsystem");
    public static final GameMode ARCADE = new GameModeWrapper("Arcade", new String[]{"Bingo"}, "bingo");
    public static final GameMode PARTY_GAMES = new GameModeWrapper("PartyGames", new String[]{"PixelPainters"}, "pixelpainters");
}
