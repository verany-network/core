package net.verany.api.setup;

import lombok.Getter;
import lombok.Setter;
import net.verany.api.AbstractVerany;
import net.verany.api.Verany;
import net.verany.api.loader.database.DatabaseLoadObject;
import net.verany.api.loader.database.DatabaseLoader;
import net.verany.api.module.VeranyProject;
import net.verany.api.setup.category.AbstractSetupCategory;
import net.verany.api.setup.category.SetupCategoryWrapper;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.WorldCreator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public abstract class AbstractSetupObject extends DatabaseLoader {

    public AbstractSetupObject(VeranyProject project, String database) {
        super(project, "locations", database);
        Verany.SETUP_OBJECTS.add(this);
    }

    public AbstractSetupObject(VeranyProject project) {
        this(project, project.getModule().name().toLowerCase());
    }

    public void saveLocations() {
        save("locations");
        load();
    }

    public void load() {
        load(new LoadInfo<>("locations", LocationDataObject.class, new LocationDataObject()));
    }

    public void loadWorlds() {
        for (AbstractSetupCategory value : getDataObject().getSetupCategoryMap().values()) {
            for (AbstractSetupCategory.LocationData location : value.getLocations()) {
                if (location.getLocation().getWorld().equals("-")) continue;
                Bukkit.createWorld(new WorldCreator(location.getLocation().getWorld()));
            }
        }
    }

    public void reload() {
        remove(getInfo(LocationDataObject.class));
        load();
    }

    public abstract void registerNewLocation(String name, AbstractSetupCategory category);

    public abstract Location getLocation(String category, String name);

    public abstract boolean existLocation(String category, String name);

    public abstract boolean isLocationSet(String category, String name);

    public abstract AbstractSetupCategory getCategory(String category);

    public abstract List<AbstractSetupCategory.LocationData> getLocations(String category);

    public AbstractSetupCategory getNewCategory(Material material) {
        return new SetupCategoryWrapper(material);
    }

    public LocationDataObject getDataObject() {
        return getDataOptional(LocationDataObject.class).orElse(null);
    }

    @Getter
    @Setter
    public static class LocationDataObject extends DatabaseLoadObject {

        private final Map<String, AbstractSetupCategory> setupCategoryMap = new HashMap<>();

        public LocationDataObject() {
            super("locations");
        }

        public Location getLocation(String category, String name) {
            return setupCategoryMap.get(category).getLocation(name).getLocation().toLocation();
        }
    }

}
