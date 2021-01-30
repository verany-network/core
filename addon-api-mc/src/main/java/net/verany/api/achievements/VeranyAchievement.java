package net.verany.api.achievements;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.util.Consumer;

@AllArgsConstructor
@Getter
public abstract class VeranyAchievement {

    private final String name;
    private final Material material;
    private final String gameMode;
    private final int difficulty;

    /**
     * Collect the achievement for {@code player} if he does not have it yet
     *
     * @param player          bukkit player
     * @param collectConsumer consumer called if method success
     */
    public abstract void collect(Player player, Consumer<VeranyAchievement> collectConsumer);

}
