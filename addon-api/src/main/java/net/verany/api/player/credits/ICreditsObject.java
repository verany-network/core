package net.verany.api.player.credits;

import net.verany.api.player.IPlayerLoader;

public interface ICreditsObject extends IPlayerLoader {

    int getCredits();

    String getCreditsAsDecimal();

    void addCredits(int amount);

    void removeCredits(int amount);

    boolean hasEnoughCredits(int amount);

}
