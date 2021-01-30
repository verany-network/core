package net.verany.api.world;

import com.besaba.revonline.pastebinapi.paste.PasteExpire;
import com.besaba.revonline.pastebinapi.paste.PasteVisiblity;
import com.besaba.revonline.pastebinapi.response.FailedResponse;
import com.besaba.revonline.pastebinapi.response.Response;
import net.verany.api.cuboid.Cuboid;
import net.verany.api.pastebin.PastebinBuilder;
import net.verany.api.world.schematic.AbstractSchematic;
import net.verany.api.world.schematic.ISchematicPaster;
import net.verany.api.world.schematic.SchematicPaster;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WorldObject implements IWorldObject {

    private Map<String, List<String>> loadedSchematics;
    private Map<String, BukkitTask> schematicTasks;
    private Map<String, Float> pasteId;

    public WorldObject() {
        loadedSchematics = new ConcurrentHashMap<>();
        schematicTasks = new ConcurrentHashMap<>();
        pasteId = new ConcurrentHashMap<>();

        new File("server/schematics/").mkdirs();
    }

    @Override
    public boolean loadSchematic(String name) {
        if (new File("server/schematics/", name + ".schem").exists()) {
            List<String> list = getStringListFromFile(name);
            if (list != null && list.size() > 0) {
                loadedSchematics.put(name, list);
                return true;
            }
        }
        return false;
    }

    @Override
    public List<String> getStringListFromFile(String fileName) {
        File file = new File("server/schematics/", fileName + ".schem");
        FileReader reader = null;
        try {
            reader = new FileReader(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader bufferedReader = new BufferedReader(reader);
        List<String> list = new ArrayList<>();
        String line = null;
        try {
            while ((line = bufferedReader.readLine()) != null) {
                list.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public boolean saveSchematic(String fileName, List<String> list) {
        File file = new File("server/schematics/", fileName + ".schem");
        String newLine = System.getProperty("line.separator");
        if (file.exists()) {
            return false;
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            for (String s : list) {
                writer.write(s);
                writer.write(newLine);
            }
            writer.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<String> fromBlockToString(List<Block> blocks, Location start) {
        List<String> list = new ArrayList<>();
        blocks.forEach(block -> list.add(blockToString(block, start)));
        return list;
    }

    @Override
    public List<Block> getBlocks(Location first, Location second) {
        Cuboid cuboid = new Cuboid(first, second);
        return new ArrayList<>(cuboid.getBlocks());
    }

    @Override
    public Map<String, List<String>> getSchematics() {
        return loadedSchematics;
    }

    @Override
    public ISchematicPaster pasteSchematic(Plugin plugin, String name, Location location, PasteType pasteType, int time, List<Location> allowedToReplace, AbstractSchematic schematic) {
        ISchematicPaster paster = getNewPaster(name, plugin);
        paster.paste(pasteType, location, time, allowedToReplace, schematic);
        return paster;
    }

    @Override
    public ISchematicPaster pasteSchematic(Plugin plugin, String name, Location location, PasteType pasteType, AbstractSchematic schematic) {
        return pasteSchematic(plugin, name, location, pasteType, 3, new ArrayList<>(), schematic);
    }

    @Override
    public ISchematicPaster getNewPaster(String name, Plugin plugin) {
        return new SchematicPaster(name, plugin);
    }

    @Override
    public void stopPasteSchematic(String name) {
        schematicTasks.get(name).cancel();
    }

    @Override
    public String blockToString(Block block, Location start) {
        int diffX = block.getX() - start.getBlockX();
        int diffY = block.getY() - start.getBlockY();
        int diffZ = block.getZ() - start.getBlockZ();

        return diffX + "~" + diffY + "~" + diffZ + "~" + block.getBlockData().getAsString(true);
    }

    @Override
    public Location getCenter(Chunk chunk) {
        Location location = new Location(chunk.getWorld(), chunk.getX() << 4, 64, chunk.getZ() << 4);
        int y = location.getWorld().getHighestBlockYAt(location.getBlockX(), location.getBlockZ());
        location.setY(y);
        return location;
    }

    @Override
    public Response<String> createSchematicPastebin(String name) {
        if (!loadedSchematics.containsKey(name)) return new FailedResponse<>("This schematic isn't loaded!");
        StringBuilder raw = new StringBuilder();
        for (String s : loadedSchematics.get(name))
            raw.append(s).append("\n");
        return PastebinBuilder.builder().title("Schematic " + name).raw(raw.toString()).expire(PasteExpire.OneWeek).visibility(PasteVisiblity.Unlisted).build().post();
    }

}
