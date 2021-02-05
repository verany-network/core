package net.verany.api.locationmanager;

import lombok.Getter;
import net.verany.api.loader.database.DatabaseLoadObject;
import net.verany.api.loader.database.DatabaseLoader;
import net.verany.api.module.VeranyModule.DatabaseConnection;
import net.verany.api.module.VeranyProject;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class AbstractLocationManager extends DatabaseLoader {

    public AbstractLocationManager(VeranyProject project, String collection, String database) {
        super(project, collection, database);
    }

    public abstract void save();

    public abstract void createLocation(String name, Location location);

    public abstract Location getLocation(String name);

    public abstract boolean existLocation(String name);

    @Getter
    public static class VeranyLocations extends DatabaseLoadObject {

        private final Map<String, VeranyLocation> locations = new HashMap<>();

        public VeranyLocations() {
            super("locations");
        }
    }

}

