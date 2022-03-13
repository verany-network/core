package net.verany.api.player;

import lombok.Getter;
import lombok.Setter;
import net.verany.api.loader.database.DatabaseLoadObject;
import net.verany.api.skin.AbstractSkinData;

import java.util.*;

@Getter
@Setter
public class PlayerEntry extends DatabaseLoadObject {

    private String name, language, prefix, ip;
    private AbstractSkinData skin;
    private Map<String, String> settingValues;
    private List<String> passedAchievements;
    private Integer credits, exp, points;
    private Long onlineTime, playTime, firstJoined, lastOnline;
    private List<String> logs;

    public PlayerEntry(UUID key, String name, String language, String prefix, int credits, Integer exp, Integer points, List<String> passedAchievements, Map<String, String> settingValues, List<String> logs) {
        super(key);
        this.name = name;
        this.language = language;
        this.prefix = prefix;
        this.settingValues = settingValues;
        this.passedAchievements = passedAchievements;
        this.credits = credits;
        this.exp = exp;
        this.points = points;
        this.firstJoined = System.currentTimeMillis();
        this.logs = logs;
    }

}
