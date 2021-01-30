package net.verany.api.world;

import com.besaba.revonline.pastebinapi.response.Response;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.verany.api.world.schematic.AbstractSchematic;
import net.verany.api.world.schematic.ISchematicPaster;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.Map;

public interface IWorldObject {

    boolean loadSchematic(String name);

    List<String> getStringListFromFile(String fileName);

    boolean saveSchematic(String fileName, List<String> list);

    List<String> fromBlockToString(List<Block> blocks, Location start);

    List<Block> getBlocks(Location first, Location second);

    Map<String, List<String>> getSchematics();

    ISchematicPaster pasteSchematic(Plugin plugin, String name, Location location, PasteType pasteType, int time, List<Location> allowedToReplace, AbstractSchematic schematic);

    ISchematicPaster pasteSchematic(Plugin plugin, String name, Location location, PasteType pasteType, AbstractSchematic schematic);

    ISchematicPaster getNewPaster(String name, Plugin plugin);

    void stopPasteSchematic(String name);

    String blockToString(Block block, Location start);

    Location getCenter(Chunk chunk);

    Response<String> createSchematicPastebin(String name);

    enum PasteType {
        FULL,
        STEP,
        BLOCK_BY_BLOCK
    }

    @AllArgsConstructor
    @Getter
    class PasteFinish {
        private final long time;
        private final List<Block> placedBlocks;
        private final FinishType finishType;

        public enum FinishType {
            SUCCESS,
            CANCELED,
            FAILED
        }
    }

}
