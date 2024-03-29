package net.verany.api.player.permission;

import de.dytanic.cloudnet.wrapper.Wrapper;
import net.verany.api.Verany;
import net.verany.api.player.permission.group.PermissionGroup;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissibleBase;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.ServerOperator;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class Permissible extends PermissibleBase {

    private final Player player;

    public Permissible(Player player) {
        super(new ServerOperator() {
            @Override
            public boolean isOp() {
                return player.isOp() || Wrapper.getInstance().getCurrentServiceInfoSnapshot().getProperties().contains("uuid") && Wrapper.getInstance().getCurrentServiceInfoSnapshot().getProperties().get("uuid", UUID.class).equals(player.getUniqueId());
            }

            public void setOp(boolean paramBoolean) {
                player.setOp(paramBoolean);
            }
        });
        this.player = player;
    }

    @Override
    public boolean hasPermission(@NotNull String inName) {
        IPermissionObject permissionObject = Verany.PROFILE_OBJECT.getPlayer(player.getUniqueId()).get().getPermissionObject();
        return permissionObject.hasPermission(inName);
    }

    @Override
    public boolean hasPermission(@NotNull Permission perm) {
        return hasPermission(perm.getName());
    }

    @Override
    public boolean isPermissionSet(@NotNull String name) {
        return hasPermission(name);
    }

    @Override
    public boolean isPermissionSet(@NotNull Permission perm) {
        return hasPermission(perm.getName());
    }
}
