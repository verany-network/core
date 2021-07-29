package net.verany.api.location;

import net.verany.api.locationmanager.AbstractLocationManager;
import net.verany.api.locationmanager.VeranyLocation;
import net.verany.api.module.VeranyProject;
import org.bukkit.Location;

public class LocationManager extends AbstractLocationManager {

    public LocationManager(VeranyProject project, String collection, String database) {
        super(project, collection, database);
        load(new LoadInfo<>("locations", VeranyLocations.class, new VeranyLocations()));
    }

    @Override
    public void save() {
        save("locations");
    }

    @Override
    public void createLocation(String name, Location location) {
        getDataOptional(VeranyLocations.class).ifPresent(veranyLocations -> veranyLocations.getLocations().put(name, VeranyLocation.toVeranyLocation(location)));
    }

    @Override
    public Location getLocation(String name) {
        if (getDataOptional(VeranyLocations.class).isEmpty()) return null;
        return getDataOptional(VeranyLocations.class).get().getLocations().get(name).toLocation();
    }

    @Override
    public boolean existLocation(String name) {
        if (getDataOptional(VeranyLocations.class).isEmpty()) return false;
        return getDataOptional(VeranyLocations.class).get().getLocations().containsKey(name);
    }
}
