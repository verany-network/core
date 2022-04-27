package net.verany.api.player.permission.group;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.verany.api.AbstractVerany;
import net.verany.api.player.IVeranyPlayer;
import org.bson.Document;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
@Setter
public abstract class AbstractPermissionGroup {

    private static Map<String, Document> properties = new HashMap<>();

    private String name;
    private String color;
    private String scoreboardId;
    private String prefix;
    private int joinPower;
    private final List<String> permissions = new ArrayList<>();
    private final List<String> children = new ArrayList<>();

    public List<AbstractPermissionGroup> getChildren() {
        return children.stream().map(PermissionGroup::getGroupByName).collect(Collectors.toList());
    }

    public List<String> getStringChildren() {
        return children;
    }

    public List<IVeranyPlayer> getPlayersInGroup() {
        return AbstractVerany.getPlayers(IVeranyPlayer.class).stream().filter(iVeranyPlayer -> iVeranyPlayer.getPermissionObject().getCurrentGroup().getGroup().getName().equals(name)).collect(Collectors.toList());
    }

    public AbstractPermissionGroup addPermission(String... permissions) {
        Collections.addAll(this.permissions, permissions);
        return this;
    }

    public Document appendProperty(String key, Object value) {
        properties.putIfAbsent(name, new Document());
        properties.get(name).put(key, value);
        return properties.get(name);
    }

    public Object getProperty(String key) {
        properties.putIfAbsent(name, new Document());
        return properties.get(name).getOrDefault(key, null);
    }
}
