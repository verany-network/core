package net.verany.api.player.permission.group;

import org.bson.Document;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class PermissionGroup extends AbstractPermissionGroup {

    public static final List<AbstractPermissionGroup> VALUES = new CopyOnWriteArrayList<>();

    public static final AbstractPermissionGroup ADMINISTRATOR = new PermissionGroup("Administrator", ChatColor.DARK_RED, "a", "Admin");
   public static final AbstractPermissionGroup PLAYER = new PermissionGroup("Player", ChatColor.GRAY, "c", "");

    public PermissionGroup(String name, ChatColor color, String scoreboardId, String prefix) {
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
