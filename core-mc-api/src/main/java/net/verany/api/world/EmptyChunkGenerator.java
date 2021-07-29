package net.verany.api.world;

import lombok.NonNull;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;

import java.util.Random;

public class EmptyChunkGenerator extends ChunkGenerator {

    @Override
    @NonNull
    public ChunkData generateChunkData(@NonNull World world, @NonNull Random random, int x, int z, @NonNull BiomeGrid biome) {
        return createChunkData(world);
    }

    @Override
    public boolean canSpawn(@NonNull World world, int x, int z) {
        return true;
    }

    @Override
    @NonNull
    public Location getFixedSpawnLocation(World world, Random random) {
        return new Location(world, 0.0D, 64.0D, 0.0D);
    }

}
