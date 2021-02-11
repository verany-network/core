package net.verany.api.player.leveling;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.verany.api.player.PlayerInfo;
import org.bukkit.entity.Player;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class LevelObject implements ILevelObject {

    private UUID uniqueId;
    private final PlayerInfo playerInfo;
    private int currentLevel;

    @Override
    public void load(UUID key) {
        this.uniqueId = key;

        this.currentLevel = getLevel();
    }

    @Override
    public void update() {
    }

    @Override
    public int getExp() {
        return playerInfo.getData(PlayerInfo.PlayerData.class).getExp();
    }

    @Override
    public int addExp(int exp) {
        int newAmount = getExp() + exp;
        playerInfo.getData(PlayerInfo.PlayerData.class).setExp(newAmount);
        checkNewLevel();
        return newAmount;
    }

    @Override
    public int getMaxExp() {
        return LevelCalculator.fullTargetExp(getLevel());
    }

    @Override
    public int getLevel() {
        return LevelCalculator.level(getExp());
    }

    @Override
    public void nextLevel() {
        Player player = playerInfo.getPlayer();
        player.sendMessage("new level (Lv:" + getLevel() + " Exp:" + getExp() + " MaxExp:" + getMaxExp());

        currentLevel = getLevel();
    }

    private void checkNewLevel() {
        if (currentLevel == getLevel()) return;
        nextLevel();
    }

}
