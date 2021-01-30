package net.verany.api.player.permission.group;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public abstract class AbstractPermissionGroup {

    private String name;
    private ChatColor color;
    private String scoreboardId;
    private String prefix;
    private int joinPower;
    private final List<String> permissions = new ArrayList<>();
    private final List<String> children = new ArrayList<>();

    public List<AbstractPermissionGroup> getChildren() {
        List<AbstractPermissionGroup> toReturn = new ArrayList<>();
        for (String child : children)
            toReturn.add(PermissionGroup.getGroupByName(child));
        return toReturn;
    }

}
