package net.verany.api.team;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;

@RequiredArgsConstructor
@Getter
public abstract class AbstractGameTeam {

    private final String name;
    private final String scoreboardId;
    private final ChatColor color;

    public String getColorName() {
        return color.toString() + name;
    }


}
