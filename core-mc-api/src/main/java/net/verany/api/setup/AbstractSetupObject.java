package net.verany.api.setup;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import net.verany.api.AbstractVerany;
import net.verany.api.Verany;
import net.verany.api.loader.database.DatabaseLoadObject;
import net.verany.api.loader.database.DatabaseLoader;
import net.verany.api.locationmanager.VeranyLocation;
import net.verany.api.map.IMapObject;
import net.verany.api.module.VeranyProject;
import net.verany.api.setup.category.AbstractSetupCategory;
import net.verany.api.setup.category.SetupCategoryWrapper;
import net.verany.api.world.IWorldObject;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.WorldCreator;

import java.util.*;
import java.util.stream.Collectors;

public abstract class AbstractSetupObject extends DatabaseLoader {

    @Setter
    @Getter
    private IMapObject mapObject = null;

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
        for (AbstractSetupCategory value : getDataObject().getCategories()) {
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

    public void setLocation(String category, String name, Location location) {
        if (getDataObject() == null) return;
        getCategory(category).getLocation(name).setLocation(VeranyLocation.fromBukkit(location));
    }

    public abstract void registerNewLocation(AbstractSetupCategory category);

    public abstract Location getLocation(String category, String name);

    public abstract boolean existLocation(String category, String name);

    public abstract boolean isLocationSet(String category, String name);

    public abstract AbstractSetupCategory getCategory(String category);

    public abstract List<AbstractSetupCategory.LocationData> getLocations(String category);

    public List<AbstractSetupCategory> getCategories() {
        if (getDataObject() == null) return new ArrayList<>();
        return getDataObject().getCategories();
    }

    public AbstractSetupCategory getNewCategory(String name, Material material) {
        if (getCategory(name) != null) return getCategory(name);
        return new SetupCategoryWrapper(name, material);
    }

    public LocationDataObject getDataObject() {
        return getDataOptional(LocationDataObject.class).orElse(null);
    }

    @Getter
    @Setter
    public static class LocationDataObject extends DatabaseLoadObject {

        private final List<AbstractSetupCategory> categories = new ArrayList<>();

        public LocationDataObject() {
            super("locations");
        }

        public Location getLocation(String category, String name) {
            if (getCategory(category) == null) return null;
            return getCategory(category).getLocation(name).getLocation().toBukkit();
        }

        public AbstractSetupCategory getCategory(String name) {
            return categories.stream().filter(abstractSetupCategory -> abstractSetupCategory.getName().equals(name)).findFirst().orElse(null);
        }
    }

}
