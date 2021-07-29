package net.verany.api.player.permission.group;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.verany.api.AbstractVerany;
import net.verany.api.player.IVeranyPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
@Setter
public abstract class AbstractPermissionGroup {

    public static final List<AbstractPermissionGroup> VALUES = new CopyOnWriteArrayList<>();

    private String name;
    private String color;
    private String scoreboardId;
    private String prefix;
    private int joinPower;
    private final List<String> permissions = new ArrayList<>();
    private final List<String> children = new ArrayList<>();

    public List<AbstractPermissionGroup> getChildren() {
        return children.stream().map(AbstractPermissionGroup::getGroupByName).collect(Collectors.toList());
    }

    public static AbstractPermissionGroup getGroupByName(String name) {
        return VALUES.stream().filter(abstractPermissionGroup -> abstractPermissionGroup.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public List<String> getStringChildren() {
        return children;
    }

    public List<IVeranyPlayer> getPlayersInGroup() {
        return AbstractVerany.PROFILE_OBJECT.getRegisteredPlayers(IVeranyPlayer.class).stream().filter(iVeranyPlayer -> iVeranyPlayer.getPermissionObject().getCurrentGroup().getGroup().getName().equals(name)).collect(Collectors.toList());
    }
}
