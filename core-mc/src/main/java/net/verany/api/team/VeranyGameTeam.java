package net.verany.api.team;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class VeranyGameTeam extends AbstractGameTeam {

    public static final List<AbstractGameTeam> TEAMS = new ArrayList<>();

    public static final AbstractGameTeam TURQUOISE = new VeranyGameTeam("turquoise", "a", ChatColor.AQUA);
    public static final AbstractGameTeam CYAN = new VeranyGameTeam("cyan", "b", ChatColor.DARK_AQUA);

    public VeranyGameTeam(String name, String scoreboardId, ChatColor color) {
        super(name, scoreboardId, color);
        TEAMS.add(this);
    }
}