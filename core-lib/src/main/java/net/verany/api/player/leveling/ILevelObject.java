package net.verany.api.player.leveling;

import net.verany.api.interfaces.IDefault;

import java.util.UUID;

public interface ILevelObject extends IDefault<UUID> {

    int getExp();

    int addExp(int exp);

    int getMaxExp();

    int getLevel();

    void nextLevel();

}
