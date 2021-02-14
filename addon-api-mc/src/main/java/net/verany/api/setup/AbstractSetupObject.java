package net.verany.api.setup;

import lombok.Getter;
import lombok.Setter;
import net.verany.api.loader.database.DatabaseLoadObject;
import net.verany.api.loader.database.DatabaseLoader;
import net.verany.api.locationmanager.VeranyLocation;
import net.verany.api.module.VeranyProject;
import net.verany.api.setup.category.AbstractSetupCategory;
import net.verany.api.setup.category.SetupCategoryWrapper;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public abstract class AbstractSetupObject extends DatabaseLoader {

    public AbstractSetupObject(VeranyProject project) {
        super(project, project.getName(), "locations");
    }

    public abstract void registerNewLocation(AbstractSetupCategory category);

    public abstract void saveLocations();

    public abstract Location getLocation(String category, String name);

    public abstract boolean existLocation(String category, String name);

    public abstract boolean isLocationSet(String category, String name);

    public AbstractSetupCategory getNewCategory(String name, Material material) {
        return new SetupCategoryWrapper(material);
    }

    @Getter
    @Setter
    public static class LocationDataObject extends DatabaseLoadObject {

        private final Map<String, AbstractSetupCategory> setupCategoryMap = new HashMap<>();

        public LocationDataObject() {
            super("locations");
        }

        public Location getLocation(String category, String name) {

        }
    }

}
