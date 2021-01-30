package net.verany.api.player.leveling;

import net.verany.api.interfaces.IDefault;
import net.verany.api.player.IVeranyPlayer;

import java.util.UUID;

public interface ILevelObject extends IDefault<UUID> {

    int getExp();

    int addExp(int exp);

    int removeExp(int exp);

    int getMaxExp();

    int setMaxExp(int maxExp);

    int getLevel();

    void nextLevel();

}
