package net.verany.api.player;

import net.verany.api.ban.IBanEntry;
import net.verany.api.player.credits.ICreditsObject;

public interface IVeranyPlayer {

    ICreditsObject getCreditsObject();

    boolean isBanned();

    void ban(IBanEntry banEntry);

    IBanEntry getBanEntry();

}
