package net.verany.api.setup.category;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.verany.api.locationmanager.VeranyLocation;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
public class AbstractSetupCategory {

    private final String name;
    private final Material material;
    private final List<LocationData> locations = new ArrayList<>();

    public void addLocation(String name, Material material) {
        if (getLocation(name) != null) return;
        locations.add(new LocationData(name, VeranyLocation.emptyLocation(), material));
    }

    public LocationData getLocation(String name) {
        return locations.stream().filter(locationData -> locationData.getName().equals(name)).findFirst().orElse(null);
    }

    public List<String> getWorlds() {
        return locations.stream().filter(locationData -> !locationData.getLocation().isEmpty()).map(locationData -> locationData.getLocation().getWorld()).collect(Collectors.toList());
    }

    @AllArgsConstructor
    @Getter
    @Setter
    public static class LocationData {
        private final String name;
        private VeranyLocation location;
        private final Material material;
    }

}
