package net.verany.api.region;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.verany.api.Verany;
import net.verany.api.cuboid.Cuboid;
import org.bukkit.Location;
import org.bukkit.entity.Player;

@Getter
@RequiredArgsConstructor
public abstract class GameRegion {

    private final String id = Verany.generateString(10);
    private final Cuboid cuboid;

    public abstract void onEnter(Player player);

    public abstract void onLeave(Player player);

    public boolean isInRegion(Location location) {
        return cuboid.isInside(location);
    }

    public void displayBorder(Player player) {
        
    }

}