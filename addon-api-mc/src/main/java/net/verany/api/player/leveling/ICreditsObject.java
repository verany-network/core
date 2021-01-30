package net.verany.api.player.leveling;

import net.verany.api.interfaces.IDefault;
import net.verany.api.player.IVeranyPlayer;

import java.util.UUID;

public interface ICreditsObject extends IDefault<UUID> {

    int getCredits();

    String getCreditsAsDecimal();

    int setCredits(int credits);

    int addCredits(int credits);

    int removeCredits(int credits);

    boolean hasEnoughCredits(int amount);

}
