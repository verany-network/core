package net.verany.api.player.permission.group;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class PermissionGroup extends AbstractPermissionGroup {

    public static final List<AbstractPermissionGroup> VALUES = new CopyOnWriteArrayList<>();

    public static final AbstractPermissionGroup ADMINISTRATOR = new PermissionGroup("Administrator", "DARK_RED", "a", "Admin");
   public static final AbstractPermissionGroup PLAYER = new PermissionGroup("Player", "GRAY", "c", "");

    public PermissionGroup(String name, String color, String scoreboardId, String prefix) {
        super(name, color, scoreboardId, prefix, 0);
        VALUES.add(this);
    }

    public static AbstractPermissionGroup getGroupByName(String name) {
        for (AbstractPermissionGroup value : VALUES)
            if (value.getName().equalsIgnoreCase(name))
                return value;
        return null;
    }
}
