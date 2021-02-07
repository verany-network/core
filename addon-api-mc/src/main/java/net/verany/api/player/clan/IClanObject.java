package net.verany.api.player.clan;

import lombok.NonNull;
import net.verany.api.player.IPlayerLoader;
import net.verany.api.player.clan.data.IClanData;

public interface IClanObject extends IPlayerLoader {

    String getClanId();

    String getClanRank();

    IClanData getClanData();

    void setClanData(@NonNull IClanData clanData);

}
