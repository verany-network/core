package net.verany.api.setup;

import net.verany.api.Verany;
import net.verany.api.module.VeranyProject;
import net.verany.api.setup.category.AbstractSetupCategory;
import org.bson.Document;
import org.bukkit.Location;

public class SetupObject extends AbstractSetupObject {

    public SetupObject(VeranyProject project) {
        super(project);
        load();
    }

    private void load() {
        load(new LoadInfo<>("locations", LocationDataObject.class, new LocationDataObject()));
    }

    @Override
    public void registerNewLocation(AbstractSetupCategory category) {
        getData(LocationDataObject.class).getSetupCategoryMap().put()
    }

    @Override
    public void saveLocations() {
        save("setup");
        load(new LoadInfo<>("locations", LocationDataObject.class, new LocationDataObject()));
    }

    @Override
    public Location getLocation(String category, String name) {
        if (!existLocation(category, name) || !isLocationSet(category, name)) return null;
        return getData(LocationDataObject.class).getLocation(category, name);
    }

    @Override
    public boolean existLocation(String category, String name) {
        return getData(LocationDataObject.class).getSetupCategoryMap().get(category).getLocations().containsKey(name);
    }

    @Override
    public boolean isLocationSet(String category, String name) {
        return existLocation(category, name) && !getData(LocationDataObject.class).getSetupCategoryMap().get(category).getLocations().get(name).getWorld().equals("-");
    }

}
