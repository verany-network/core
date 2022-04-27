package net.verany.api.setup;

import net.verany.api.module.VeranyProject;
import net.verany.api.setup.category.AbstractSetupCategory;
import org.bukkit.Location;

import java.util.List;

public class SetupObject extends AbstractSetupObject {
    public SetupObject(VeranyProject project, String database) {
        super(project, database);
        load();
    }

    public SetupObject(VeranyProject project) {
        super(project);
        load();
    }

    @Override
    public void registerNewLocation(AbstractSetupCategory category) {
        //if (getCategories().containsKey(name)) return;
        getCategories().removeIf(abstractSetupCategory -> abstractSetupCategory.getName().equals(category.getName()));
        getCategories().add(category);
    }

    @Override
    public Location getLocation(String category, String name) {
        if (!existLocation(category, name) || !isLocationSet(category, name)) return null;
        return getData(LocationDataObject.class).getLocation(category, name);
    }

    @Override
    public boolean existLocation(String category, String name) {
        if (getDataObject() == null || (getDataObject() != null && getDataObject().getCategory(category) == null))
            return false;
        return getDataObject().getCategory(category).getLocation(name) != null;
    }

    @Override
    public boolean isLocationSet(String category, String name) {
        return existLocation(category, name) && !getDataObject().getCategory(category).getLocation(name).getLocation().getWorld().equals("-");
    }

    @Override
    public AbstractSetupCategory getCategory(String category) {
        return getDataObject().getCategory(category);
    }

    @Override
    public List<AbstractSetupCategory.LocationData> getLocations(String category) {
        return getCategory(category).getLocations();
    }
}
