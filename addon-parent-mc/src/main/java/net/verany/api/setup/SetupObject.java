package net.verany.api.setup;

import net.verany.api.Verany;
import net.verany.api.module.VeranyProject;
import net.verany.api.setup.category.AbstractSetupCategory;
import org.bson.Document;
import org.bukkit.Location;

import java.util.List;

public class SetupObject extends AbstractSetupObject {

    public SetupObject(VeranyProject project) {
        super(project);
        load();
    }

    @Override
    public void registerNewLocation(String name, AbstractSetupCategory category) {
        if (getDataObject().getSetupCategoryMap().containsKey(name)) return;
        getDataObject().getSetupCategoryMap().put(name, category);
    }

    @Override
    public Location getLocation(String category, String name) {
        if (!existLocation(category, name) || !isLocationSet(category, name)) return null;
        return getData(LocationDataObject.class).getLocation(category, name);
    }

    @Override
    public boolean existLocation(String category, String name) {
        if (!getDataObject().getSetupCategoryMap().containsKey(category)) return false;
        return getDataObject().getSetupCategoryMap().get(category).getLocation(name) != null;
    }

    @Override
    public boolean isLocationSet(String category, String name) {
        return existLocation(category, name) && !getDataObject().getSetupCategoryMap().get(category).getLocation(name).getLocation().getWorld().equals("-");
    }

    @Override
    public AbstractSetupCategory getCategory(String category) {
        return getDataObject().getSetupCategoryMap().get(category);
    }

    @Override
    public List<AbstractSetupCategory.LocationData> getLocations(String category) {
        return getCategory(category).getLocations();
    }
}
