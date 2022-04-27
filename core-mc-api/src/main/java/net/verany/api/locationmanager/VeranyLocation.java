package net.verany.api.locationmanager;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;

@AllArgsConstructor
@Getter
public class VeranyLocation {

    private final long timestamp = System.currentTimeMillis();
    private final String world;
    private final double x, y, z;
    private final float yaw, pitch;

    public Location toBukkit() {
        return new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
    }

    public static VeranyLocation emptyLocation() {
        return new VeranyLocation("-", -1, -1, -1, -1, -1);
    }

    public static VeranyLocation fromBukkit(Location location) {
        return new VeranyLocation(location.getWorld().getName(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }

    public boolean isEmpty() {
        return world.equals("-");
    }

    public String toJson() {
        return new Gson().toJson(this);
    }

    public static VeranyLocation fromJson(String json) {
        return new Gson().fromJson(json, VeranyLocation.class);
    }
}
