package net.verany.api.player.permission.group;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class PermissionGroup extends AbstractPermissionGroup {

    public static final List<AbstractPermissionGroup> VALUES = new CopyOnWriteArrayList<>();

    public static final AbstractPermissionGroup ADMINISTRATOR = new PermissionGroup("Administrator", "DARK_RED", "a", "Admin").addPermission("*");
    public static final AbstractPermissionGroup PLAYER = new PermissionGroup("Player", "GRAY", "c", "");

    public PermissionGroup(String name, String color, String scoreboardId, String prefix) {
        super(name, color, scoreboardId, prefix, 0);
        VALUES.add(this);
    }

    public static AbstractPermissionGroup getGroupByName(String name) {
        return VALUES.stream().filter(abstractPermissionGroup -> abstractPermissionGroup.getName().equalsIgnoreCase(name)).findFirst().orElse(PLAYER);
    }

    public static boolean existGroup(String name) {
        return VALUES.stream().anyMatch(abstractPermissionGroup -> abstractPermissionGroup.getName().equals(name));
    }

    public static AbstractPermissionGroup getGroupByProperty(String key, Object value) {
        return VALUES.stream().filter(abstractPermissionGroup -> abstractPermissionGroup.getProperty(key).equals(value)).findFirst().orElse(null);
    }
}
