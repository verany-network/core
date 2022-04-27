package net.verany.api.rating;

import lombok.Getter;
import net.verany.api.loader.database.DatabaseLoader;
import net.verany.api.module.VeranyProject;
import net.verany.api.raiting.IRatingObject;

import java.util.List;
import java.util.UUID;

@Getter
public class RatingObject<T> extends DatabaseLoader implements IRatingObject<T> {

    private UUID uniqueId;

    public RatingObject(VeranyProject project, String collection, String database) {
        super(project, collection, database);
    }

    @Override
    public void load(UUID key) {
        uniqueId = key;

        load(new LoadInfo<>("rating", RatingObjectLoader.class, new RatingObjectLoader<>(key)));
    }

    @Override
    public void update() {

    }

    @Override
    public void rate(UUID uuid, T entry, double value) {
        if (getDataOptional(RatingObjectLoader.class).isEmpty()) return;
        getDataOptional(RatingObjectLoader.class).get().rate(entry, value);
    }

    @Override
    public double getRating(T entry) {
        return 0;
    }

    @Override
    public boolean hasRated(T entry) {
        return false;
    }

    @Override
    public double getGlobalRating(T entry) {
        return 0;
    }

    @Override
    public List<T> getValues() {
        return null;
    }
}
