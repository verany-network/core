package net.verany.api.player.permission.group;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.verany.api.AbstractVerany;
import net.verany.api.player.IPlayerInfo;
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

    public List<String> getStringChildren() {
        return children;
    }

    public List<IPlayerInfo> getPlayersInGroup() {
        List<IPlayerInfo> toReturn = new ArrayList<>();
        for (IPlayerInfo playerInfo : AbstractVerany.PROFILE_OBJECT.getRegisteredPlayers())
            if (playerInfo.getPermissionObject().getCurrentGroup().getGroup().getName().equals(name))
                toReturn.add(playerInfo);
        return toReturn;
    }
}
