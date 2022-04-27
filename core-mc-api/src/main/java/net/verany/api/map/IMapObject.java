package net.verany.api.map;

import net.verany.api.map.data.MapEntry;
import net.verany.api.map.data.MapRating;
import org.bukkit.Material;

import java.util.List;
import java.util.UUID;

public interface IMapObject {

    void saveWorlds();

    void load();

    List<MapEntry> getMaps();

    List<MapEntry> getMapsWithoutDuplicate();

    List<MapEntry> getMapsWithoutDuplicate(String mapType);

    List<MapEntry> getMaps(String mapType);

    MapEntry getRandomMap(String mapType);

    MapEntry getRandomMap();

    MapEntry getMap(String name);

    MapEntry getMap(Material material);

    List<MapEntry> getRandomMaps(int amount);

    List<MapEntry> getRandomMaps(String mapType, int amount);

    MapEntry getMap(Material material, String mapType);

    List<MapEntry> getMapsOfTheDay();

    List<MapEntry> getMaps(List<String> maps);

    void rateMap(UUID uuid, String name, double rating);

    double getRating(String name);

    boolean hasRated(UUID uuid, String name);

    MapRating getRating(UUID uuid, String name);

}
