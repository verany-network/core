package net.verany.api.locationmanager;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;

@AllArgsConstructor
@Getter
public class VeranyLocation {

    private final String world;
    private final double x, y, z;
    private final float yaw, pitch;

    public Location toLocation() {
        return new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
    }

    public static VeranyLocation emptyLocation() {
        return new VeranyLocation("-", -1, -1, -1, -1, -1);
    }

    public static VeranyLocation toVeranyLocation(Location location) {
        return new VeranyLocation(location.getWorld().getName(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }
}
