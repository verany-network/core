package net.verany.api.player.permission;

import lombok.Getter;
import lombok.Setter;
import net.verany.api.interfaces.IDefault;
import net.verany.api.player.permission.duration.AbstractGroupTime;
import net.verany.api.player.permission.group.AbstractPermissionGroup;
import net.verany.api.player.permission.group.PermissionGroup;

import java.util.List;
import java.util.UUID;

public interface IPermissionObject extends IDefault<UUID> {

    void setGroup(GroupData group);

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
        private final long timestamp = System.currentTimeMillis();
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

}
