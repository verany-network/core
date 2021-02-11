package net.verany.api.player.nick;

import net.verany.api.player.IPlayerLoader;
import net.verany.api.player.permission.group.AbstractPermissionGroup;

public interface INickObject extends IPlayerLoader {

    void nick();

    void unNick();

    boolean isNicked();

    AbstractPermissionGroup getNickGroup();

    void setNickGroup(AbstractPermissionGroup nickGroup);

}
