package net.verany.api.world.schematic;

import lombok.Data;
import net.verany.api.Verany;
import net.verany.api.cuboid.Cuboid;
import net.verany.api.world.BlockInfo;
import net.verany.api.world.IWorldObject;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

@Data
public class SchematicPaster implements ISchematicPaster {

    private final String schematicName;
    private final Plugin plugin;

    private BukkitTask task;
    private boolean pause;
    private float pasteId;
    private long current = 0L;

    private AbstractSchematic schematic;
    private List<Block> placedBlocks;

    public void paste(IWorldObject.PasteType pasteType, Location location, int time, List<Location> allowedToReplace, AbstractSchematic schematic) {
        this.schematic = schematic;

        List<String> list = Verany.WORLD_OBJECT.getSchematics().get(schematicName);
        placedBlocks = new ArrayList<>();
        current = System.currentTimeMillis();

        switch (pasteType) {
            case FULL: {

                for (String str : list) {
                    if (str != null && !str.equals(" ")) {
                        String[] s = str.split("~");
                        Block block = getBlock(location, s);
                        block.setBlockData(Bukkit.createBlockData(s[3]));
                        placedBlocks.add(block);
                    }
                }
                schematic.onFinish(new IWorldObject.PasteFinish(System.currentTimeMillis() - current, placedBlocks, IWorldObject.PasteFinish.FinishType.SUCCESS));
                break;
            }
            case STEP: {
                Cuboid cuboid = new Cuboid(getBlock(location, list.get(0).split("~")).getLocation(), getBlock(location, list.get(list.size() - 1).split("~")).getLocation());

                Location lower = cuboid.getLowerNE();
                Location upper = cuboid.getUpperSW().clone();
                int maxLength = upper.getBlockY() - lower.getBlockY();
                upper.setY(lower.getBlockY());

                pasteId = 0;

                task = new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (pause) return;

                        float percent = (pasteId / maxLength * 100F);
                        List<Block> blocks = new ArrayList<>();

                        Cuboid pasteCuboid = new Cuboid(lower.clone().add(0, pasteId, 0), upper.clone().add(0, pasteId, 0));
                        for (String s : list) {
                            Block block = getBlock(location, s.split("~"));
                            //if (!pasteAir && !block.getType().isAir()) continue;
                            if (pasteCuboid.isInside(block.getLocation())) {
                                if (allowedToReplace.contains(block.getLocation()) || block.getType().isAir()) {
                                    BlockData blockData = Bukkit.createBlockData(s.split("~")[3]);
                                    if (blockData.getMaterial().isAir()) continue;
                                    block.setBlockData(blockData);
                                    placedBlocks.add(block);
                                    blocks.add(block);
                                }
                            }
                        }
                        schematic.onPlace(percent, blocks);

                        pasteId++;

                        if (pasteId == maxLength) {
                            stop();
                            schematic.onFinish(new IWorldObject.PasteFinish(System.currentTimeMillis() - current, placedBlocks, IWorldObject.PasteFinish.FinishType.SUCCESS));
                        }
                    }
                }.runTaskTimer(plugin, 0, time);
                break;
            }
            case BLOCK_BY_BLOCK: {
                List<BlockInfo> toPaste = new ArrayList<>();
                for (String s : list) {
                    BlockData blockData = Bukkit.createBlockData(s.split("~")[3]);
                    if (!blockData.getMaterial().isAir())
                        toPaste.add(new BlockInfo(s, blockData));
                }

                pasteId = 0;

                task = new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (pause) return;

                        float percent = (pasteId / toPaste.size() * 100F);
                        List<Block> blocks = new ArrayList<>();

                        BlockInfo s = toPaste.get((int) pasteId);
                        Block block = getBlock(location, s.getInfo().split("~"));
                        if (allowedToReplace.contains(block.getLocation()) || block.getType().isAir()) {
                            block.setBlockData(s.getBlockData());
                            placedBlocks.add(block);
                            blocks.add(block);
                        }

                        schematic.onPlace(percent, blocks);

                        pasteId++;

                        if (pasteId == toPaste.size()) {
                            stop();
                            schematic.onFinish(new IWorldObject.PasteFinish(System.currentTimeMillis() - current, placedBlocks, IWorldObject.PasteFinish.FinishType.SUCCESS));
                        }
                    }
                }.runTaskTimer(plugin, 0, time);
                break;
            }
        }
    }

    private Block getBlock(Location location, String[] s) {
        int x = Integer.parseInt(s[0]);
        int y = Integer.parseInt(s[1]);
        int z = Integer.parseInt(s[2]);
        return location.clone().add(x, y, z).getBlock();
    }

    public void pause() {
        pause = true;
    }

    public void resume() {
        pause = false;
    }

    public void stop() {
        task.cancel();
        if (schematic != null) {
            schematic.onFinish(new IWorldObject.PasteFinish(System.currentTimeMillis() - current, placedBlocks, IWorldObject.PasteFinish.FinishType.CANCELED));
        }
    }
}
