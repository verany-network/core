package net.verany.api.player.afk;

import net.verany.api.player.IPlayerLoader;

public interface IAFKObject extends IPlayerLoader {

    boolean isAfk();

    void setAfk(boolean afk);

    long getAfkTime();

    boolean shouldAfk();

    void resetAfkTime();

    void spawnHologram();

    void removeHologram();

    void updateHologram();

    boolean isCheckEnabled(CheckType checkType);

    void enableAfkCheck(CheckType... checkType);

    void disableAfkCheck(CheckType... checkType);

    enum CheckType {
        MOVE,
        SNEAK,
        INTERACT,
        CLICK,
        CHAT,
        ITEM
    }

}
