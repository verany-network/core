package net.verany.api.raiting;

import net.verany.api.interfaces.IDefault;
import net.verany.api.loader.database.DatabaseLoadObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface IRatingObject<T> extends IDefault<UUID> {

    void rate(UUID uuid, T entry, double value);

    double getRating(T entry);

    boolean hasRated(T entry);

    double getGlobalRating(T entry);

    List<T> getValues();

    class RatingObjectLoader<T> extends DatabaseLoadObject {

        private final Map<T, Double> rating = new HashMap<>();

        public RatingObjectLoader(UUID uuid) {
            super(uuid);
        }

        public void rate(T t, double value){
            rating.put(t, value);
        }

        public double getRating(T t) {
            return rating.get(t);
        }
    }

}
