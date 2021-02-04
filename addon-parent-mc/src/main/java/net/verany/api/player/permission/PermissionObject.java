package net.verany.api.player.permission;

import lombok.Getter;
import lombok.Setter;
import net.verany.api.language.EnumLanguage;
import net.verany.api.loader.database.DatabaseLoadObject;
import net.verany.api.loader.database.DatabaseLoader;
import net.verany.api.module.VeranyProject;
import net.verany.api.player.leveling.ICreditsObject;
import net.verany.api.player.permission.duration.AbstractGroupTime;
import net.verany.api.player.permission.duration.GroupTime;
import net.verany.api.player.permission.group.AbstractPermissionGroup;
import net.verany.api.player.permission.group.PermissionGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Getter
public class PermissionObject extends DatabaseLoader implements IPermissionObject {

    private UUID uniqueId;

    public PermissionObject(VeranyProject project) {
        super(project, "players", "rank");
    }

    @Override
    public void load(UUID key) {
        this.uniqueId = key;

        load();
    }

    @Override
    public void update() {
        save("user_rank");
        load();
    }

    private void load() {
        load(new LoadInfo<>("user_rank", PlayerRank.class, new PlayerRank(uniqueId.toString(), new GroupData(PermissionGroup.PLAYER, GroupTime.LIFETIME), new GroupData(PermissionGroup.PLAYER, GroupTime.LIFETIME))));
    }

    @Override
    public void setGroup(GroupData group) {
        GroupData lastGroup = getCurrentGroup();
        lastGroup.setDuration(new AbstractGroupTime.GroupDuration(lastGroup.getDuration().getAmount(), lastGroup.getTimeLeft(), lastGroup.getDuration().getKey()));
        getData(PlayerRank.class).setLastGroup(lastGroup);
        getData(PlayerRank.class).setCurrentGroup(group);
        update();
    }

    @Override
    public GroupData getCurrentGroup() {
        return getData(PlayerRank.class).getCurrentGroup();
    }

    @Override
    public List<String> getPermissions() {
        return getData(PlayerRank.class).getPermissions();
    }

    @Override
    public void addPermission(String permission) {
        getData(PlayerRank.class).getPermissions().add(permission);
        update();
    }

    @Override
    public void removePermission(String permission) {
        getData(PlayerRank.class).getPermissions().remove(permission);
        update();
    }

    @Override
    public boolean hasPermission(String permission) {
        boolean hasChildrenPermission = false;
        for (AbstractPermissionGroup child : getCurrentGroup().getGroup().getChildren())
            if (child.getPermissions().contains(permission)) {
                hasChildrenPermission = true;
                break;
            }
        return getPermissions().contains(permission) || getPermissions().contains("*") || hasChildrenPermission || getCurrentGroup().getGroup().getPermissions().contains(permission) || getCurrentGroup().getGroup().getPermissions().contains("*");
    }

    @Override
    public GroupData getLastGroup() {
        return getData(PlayerRank.class).getLastGroup();
    }

    @Getter
    @Setter
    public static class PlayerRank extends DatabaseLoadObject {

        private GroupData currentGroup;
        private GroupData lastGroup;
        private final List<String> permissions = new ArrayList<>();

        public PlayerRank(String uuid, GroupData currentGroup, GroupData lastGroup) {
            super(uuid);
            this.currentGroup = currentGroup;
            this.lastGroup = lastGroup;
        }
    }
}
