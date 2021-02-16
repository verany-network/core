package net.verany.api.setup.category;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.verany.api.locationmanager.VeranyLocation;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Getter
public abstract class AbstractSetupCategory {

    private final Material material;
    private final List<LocationData> locations = new ArrayList<>();

    public void addLocation(String name, Material material) {
        locations.add(new LocationData(name, VeranyLocation.emptyLocation(), material));
    }

    public LocationData getLocation(String name) {
        for (LocationData location : locations)
            if (location.getName().equalsIgnoreCase(name))
                return location;
        return null;
    }

    @AllArgsConstructor
    @Getter
    public static class LocationData {
        private final String name;
        private final VeranyLocation location;
        private final Material material;
    }

}
