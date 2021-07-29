package net.verany.api.achievement;

import net.verany.api.Verany;
import net.verany.api.achievements.VeranyAchievement;
import net.verany.api.player.IPlayerInfo;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.util.Consumer;

public class VeranyAchievementWrapper extends VeranyAchievement {

    public VeranyAchievementWrapper(String name, Material material, String gameMode, int difficulty) {
        super(name, material, gameMode, difficulty);
        if (VeranyAchievementType.getAchievementByName(name) == null)
            VeranyAchievementType.ACHIEVEMENTS.add(this);
    }

    @Override
    public void collect(Player player, Consumer<VeranyAchievement> collectConsumer) {
        collectConsumer.accept(this);
        Verany.PROFILE_OBJECT.getPlayer(player.getUniqueId(), IPlayerInfo.class).get().passAchievement(this);
    }

    public VeranyAchievementWrapper doNotAdd() {
        VeranyAchievementType.ACHIEVEMENTS.remove(this);
        return this;
    }

}
