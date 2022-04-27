package net.verany.api.world;

import net.verany.api.world.schematic.AbstractSchematic;
import net.verany.api.world.schematic.ISchematicPaster;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.Map;

public class WorldObject implements IWorldObject {
    @Override
    public boolean loadSchematic(String name) {
        return false;
    }

    @Override
    public List<String> getStringListFromFile(String fileName) {
        return null;
    }

    @Override
    public boolean saveSchematic(String fileName, List<String> list) {
        return false;
    }

    @Override
    public List<String> fromBlockToString(List<Block> blocks, Location start) {
        return null;
    }

    @Override
    public List<Block> getBlocks(Location first, Location second) {
        return null;
    }

    @Override
    public Map<String, List<String>> getSchematics() {
        return null;
    }

    @Override
    public ISchematicPaster pasteSchematic(Plugin plugin, String name, Location location, PasteType pasteType, int time, List<Location> allowedToReplace, AbstractSchematic schematic) {
        return null;
    }

    @Override
    public ISchematicPaster pasteSchematic(Plugin plugin, String name, Location location, PasteType pasteType, AbstractSchematic schematic) {
        return null;
    }

    @Override
    public ISchematicPaster getNewPaster(String name, Plugin plugin) {
        return null;
    }

    @Override
    public void stopPasteSchematic(String name) {

    }

    @Override
    public String blockToString(Block block, Location start) {
        return null;
    }

    @Override
    public Location getCenter(Chunk chunk) {
        return null;
    }
}
