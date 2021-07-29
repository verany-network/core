package net.verany.api.player;

import lombok.Getter;
import lombok.Setter;
import net.verany.api.loader.database.DatabaseLoadObject;
import net.verany.api.skin.AbstractSkinData;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
public class PlayerEntry extends DatabaseLoadObject {

    private String name, language, prefix;
    private AbstractSkinData skin;
    private Map<String, Object> settingValues;
    private List<String> passedAchievements;
    private Integer credits, exp, points;
    private Long onlineTime, playTime, firstJoined, lastOnline;
    private List<String> logs;

    public PlayerEntry(UUID id) {
        super(id);
    }

}
