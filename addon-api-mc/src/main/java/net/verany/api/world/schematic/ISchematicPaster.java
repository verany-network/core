package net.verany.api.world.schematic;

import com.besaba.revonline.pastebinapi.response.Response;
import net.verany.api.world.IWorldObject;
import org.bukkit.Location;

import java.util.List;

public interface ISchematicPaster {

    /**
     * Paste the schematic
     *
     * @param pasteType        type of place
     * @param location         location of schematic
     * @param time             time need to place
     * @param allowedToReplace blocks that are able to replace
     * @param schematic        schematic
     */
    void paste(IWorldObject.PasteType pasteType, Location location, int time, List<Location> allowedToReplace, AbstractSchematic schematic);

    /**
     * Pause the pasting
     */
    void pause();

    /**
     * Resume the pasting
     */
    void resume();

    /**
     * Stop the pasting
     */
    void stop();

}
