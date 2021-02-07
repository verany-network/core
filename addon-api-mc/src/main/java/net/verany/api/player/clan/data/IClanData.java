package net.verany.api.player.clan.data;

import net.verany.api.interfaces.IDefault;
import net.verany.api.player.permission.group.AbstractPermissionGroup;

import java.util.List;

public interface IClanData extends IDefault<String> {

    String getId();

    String getName();

    String getTag();

    List<AbstractPermissionGroup> getClanRanks();

}
