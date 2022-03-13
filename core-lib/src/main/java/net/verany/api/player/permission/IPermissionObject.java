package net.verany.api.player.permission;

import lombok.Getter;
import lombok.Setter;
import net.verany.api.interfaces.IDefault;
import net.verany.api.loader.database.DatabaseLoadObject;
import net.verany.api.player.permission.duration.AbstractGroupTime;
import net.verany.api.player.permission.group.AbstractPermissionGroup;
import net.verany.api.player.permission.group.PermissionGroup;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public interface IPermissionObject extends IDefault<UUID> {

    void setGroup(GroupData group, Consumer<GroupData> onFinish);

    GroupData getCurrentGroup();

    List<String> getPermissions();

    void addPermission(String permission);

    void removePermission(String permission);

    boolean hasPermission(String permission);

    GroupData getLastGroup();

    @Getter
    @Setter
    class GroupData {
        private final String group;
        private final Long timestamp = System.currentTimeMillis();
        private AbstractGroupTime.GroupDuration duration;

        public GroupData(AbstractPermissionGroup group, AbstractGroupTime.GroupDuration duration) {
            this.group = group.getName();
            this.duration = duration;
        }

        public long getTimeLeft() {
            if (duration.getMillis() == -1)
                return -1;
            return (timestamp + duration.getMillis()) - System.currentTimeMillis();
        }

        public boolean isExpired() {
            return getTimeLeft() != -1 && getTimeLeft() <= 0;
        }

        public AbstractPermissionGroup getGroup() {
            return PermissionGroup.getGroupByName(group);
        }
    }

    @Getter
    @Setter
    class PlayerRank extends DatabaseLoadObject {

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
