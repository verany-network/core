package net.verany.api.player.permission;

import de.dytanic.cloudnet.common.document.gson.JsonDocument;
import de.dytanic.cloudnet.driver.CloudNetDriver;
import lombok.Getter;
import lombok.Setter;
import net.verany.api.loader.database.DatabaseLoadObject;
import net.verany.api.loader.database.DatabaseLoader;
import net.verany.api.module.VeranyProject;
import net.verany.api.player.PlayerInfo;
import net.verany.api.player.permission.duration.AbstractGroupTime;
import net.verany.api.player.permission.duration.GroupTime;
import net.verany.api.player.permission.group.AbstractPermissionGroup;
import net.verany.api.player.permission.group.PermissionGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class PermissionObject extends DatabaseLoader implements IPermissionObject {

    private final PlayerInfo playerInfo;
    private UUID uniqueId;

    public PermissionObject(PlayerInfo playerInfo, VeranyProject project) {
        super(project, "players", "rank");
        this.playerInfo = playerInfo;
    }

    @Override
    public void load(UUID key) {
        this.uniqueId = key;

        load();
    }

    @Override
    public void update() {
        remove(getInfo(PlayerRank.class));
        load();
    }

    private void load() {
        load(new LoadInfo<>("user_rank", PlayerRank.class, new PlayerRank(uniqueId, new GroupData(PermissionGroup.PLAYER, GroupTime.LIFETIME), new GroupData(PermissionGroup.PLAYER, GroupTime.LIFETIME), new ArrayList<>())));
    }

    @Override
    public void setGroup(GroupData group) {
        GroupData lastGroup = getCurrentGroup();
        lastGroup.setDuration(new AbstractGroupTime.GroupDuration(lastGroup.getDuration().getAmount(), lastGroup.getTimeLeft(), lastGroup.getDuration().getKey()));
        getData(PlayerRank.class).setLastGroup(lastGroup);
        getData(PlayerRank.class).setCurrentGroup(group);
        update();

        if (group.getGroup().getName().contains("Developer")) {
            CloudNetDriver.getInstance().getMessenger().sendChannelMessage("core", "createDev", JsonDocument.newDocument().append("uuid", uniqueId));
        }
    }

    @Override
    public GroupData getCurrentGroup() {
        if (getDataOptional(PlayerRank.class).isEmpty())
            return new GroupData(PermissionGroup.PLAYER, GroupTime.LIFETIME);
        return getDataOptional(PlayerRank.class).get().getCurrentGroup();
    }

    @Override
    public List<String> getPermissions() {
        if (getDataOptional(PlayerRank.class).isEmpty()) return new ArrayList<>();
        return getDataOptional(PlayerRank.class).get().getPermissions();
    }

    @Override
    public void addPermission(String permission) {
        if (getDataOptional(PlayerRank.class).isEmpty()) return;
        getDataOptional(PlayerRank.class).get().getPermissions().add(permission);
        update();
    }

    @Override
    public void removePermission(String permission) {
        if (getDataOptional(PlayerRank.class).isEmpty()) return;
        getDataOptional(PlayerRank.class).get().getPermissions().remove(permission);
        update();
    }

    @Override
    public boolean hasPermission(String permission) {
        List<String> permissions = getPermissions();
        /*boolean hasChildrenPermission = false;
        for (AbstractPermissionGroup child : getCurrentGroup().getGroup().getChildren())
            if (child.getPermissions().stream().anyMatch(s -> s.equalsIgnoreCase(permission))) {
                hasChildrenPermission = true;
                break;
            }*/
        return permissions.stream().anyMatch(s -> s.equalsIgnoreCase(permission)) || permissions.contains("*") || /*hasChildrenPermission || */getCurrentGroup().getGroup().getPermissions().contains(permission) || getCurrentGroup().getGroup().getPermissions().contains("*");
    }

    @Override
    public GroupData getLastGroup() {
        if (getDataOptional(PlayerRank.class).isEmpty())
            return new GroupData(PermissionGroup.PLAYER, GroupTime.LIFETIME);
        return getDataOptional(PlayerRank.class).get().getLastGroup();
    }

    @Getter
    @Setter
    public static class PlayerRank extends DatabaseLoadObject {

        private GroupData currentGroup;
        private GroupData lastGroup;
        private final List<String> permissions;

        public PlayerRank(UUID uuid, GroupData currentGroup, GroupData lastGroup, List<String> permissions) {
            super(uuid);
            this.currentGroup = currentGroup;
            this.lastGroup = lastGroup;
            this.permissions = permissions;
        }
    }
}
