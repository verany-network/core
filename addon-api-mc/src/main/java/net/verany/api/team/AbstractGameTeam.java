package net.verany.api.team;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.verany.volcano.round.AbstractVolcanoRound;
import org.bukkit.ChatColor;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Getter
public abstract class AbstractGameTeam {

    private final String name;
    private final String scoreboardId;
    private final ChatColor color;
    private final Map<AbstractVolcanoRound, JSONObject> properties = new HashMap<>();

    public String getColorName() {
        return color.toString() + name;
    }

    public JSONObject getProperties(AbstractVolcanoRound round) {
        return properties.getOrDefault(round, null);
    }

}
