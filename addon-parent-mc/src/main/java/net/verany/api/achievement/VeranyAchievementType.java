package net.verany.api.achievement;

import net.verany.api.achievements.VeranyAchievement;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public abstract class VeranyAchievementType {

    public static final List<VeranyAchievement> ACHIEVEMENTS = new ArrayList<>();

    public static final VeranyAchievement ACCESS_ACHIEVEMENT = new VeranyAchievementWrapper("ACCESS", Material.BEACON, "SERVER", 0);

    /**
     * @param name name of achievement
     * @return returns {@link VeranyAchievement VeranyAchievement.class} from {@code name}
     */
    public static VeranyAchievement getAchievementByName(String name) {
        for (VeranyAchievement achievement : ACHIEVEMENTS)
            if (achievement.getName().equalsIgnoreCase(name))
                return achievement;
        return null;
    }

    /**
     * @param gameMode gamemode of achievement
     * @return returns {@link VeranyAchievement VeranyAchievement.class} from {@code gameMode}
     */
    public static List<VeranyAchievement> getAchievementByGameMode(String gameMode) {
        List<VeranyAchievement> toReturn = new ArrayList<>();
        for (VeranyAchievement achievement : ACHIEVEMENTS)
            if (achievement.getGameMode().equals(gameMode))
                toReturn.add(achievement);
        return toReturn;
    }

    /**
     * @param achievements string list of achievement names
     * @return returns list of {@link VeranyAchievement VeranyAchievement.class} from names of {@code achievements}
     */
    public static List<VeranyAchievement> getAchievementByStringList(List<String> achievements) {
        List<VeranyAchievement> toReturn = new ArrayList<>();
        for (String achievement : achievements)
            toReturn.add(getAchievementByName(achievement));
        return toReturn;
    }

}
