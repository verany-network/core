package net.verany.api.world.schematic;

import net.verany.api.world.IWorldObject;
import org.bukkit.block.Block;

import java.util.List;

public abstract class AbstractSchematic {

    /**
     * Method called when blocks are placed
     *
     * @param percent      percentage of place progress
     * @param placedBlocks placed blocks in task
     */
    public void onPlace(float percent, List<Block> placedBlocks) {

    }

    /**
     * Method called when paste finished
     *
     * @param finish finish data
     */
    public abstract void onFinish(IWorldObject.PasteFinish finish);

}
