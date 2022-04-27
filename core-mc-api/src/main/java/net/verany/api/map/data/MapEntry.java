package net.verany.api.map.data;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bson.Document;
import org.bukkit.Material;

import java.util.List;

@RequiredArgsConstructor
@Getter
@Setter
public class MapEntry {

    private final String name;
    private final String mapType;
    private final boolean enabled;
    private final Material material;
    private final List<String> builder;
    private final List<MapRating> ratings;
    private final Document properties;
    private float averageWinChance;

    public double getRating() {
        double toReturn = 0;
        for (MapRating mapRating : ratings)
            toReturn += mapRating.getRating();
        return toReturn / ratings.size();
    }

}
