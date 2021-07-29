
package net.verany.api.cuboid;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;

import java.util.*;

public class Cuboid implements Iterable<Block>, Cloneable, ConfigurationSerializable {

    protected final String worldName;
    protected int x1;
    protected int y1;
    protected int z1;
    protected int x2;
    protected int y2;
    protected int z2;

    public Cuboid(Location l1, Location l2) {
        if (!l1.getWorld().equals(l2.getWorld())) {
            throw new IllegalArgumentException("Locations must be on the same world");
        }
        this.worldName = l1.getWorld().getName();
        this.x1 = Math.min(l1.getBlockX(), l2.getBlockX());
        this.y1 = Math.min(l1.getBlockY(), l2.getBlockY());
        this.z1 = Math.min(l1.getBlockZ(), l2.getBlockZ());
        this.x2 = Math.max(l1.getBlockX(), l2.getBlockX());
        this.y2 = Math.max(l1.getBlockY(), l2.getBlockY());
        this.z2 = Math.max(l1.getBlockZ(), l2.getBlockZ());
    }

    public Cuboid(Location l1) {
        this(l1, l1);
    }

    public Cuboid(Cuboid other) {
        this(other.getWorld().getName(), other.x1, other.y1, other.z1, other.x2, other.y2, other.z2);
    }

    public Cuboid(World world, int x1, int y1, int z1, int x2, int y2, int z2) {
        this.worldName = world.getName();
        this.x1 = Math.min(x1, x2);
        this.x2 = Math.max(x1, x2);
        this.y1 = Math.min(y1, y2);
        this.y2 = Math.max(y1, y2);
        this.z1 = Math.min(z1, z2);
        this.z2 = Math.max(z1, z2);
    }

    private Cuboid(String worldName, int x1, int y1, int z1, int x2, int y2, int z2) {
        this.worldName = worldName;
        this.x1 = Math.min(x1, x2);
        this.x2 = Math.max(x1, x2);
        this.y1 = Math.min(y1, y2);
        this.y2 = Math.max(y1, y2);
        this.z1 = Math.min(z1, z2);
        this.z2 = Math.max(z1, z2);
    }

    public Cuboid(Map<String, Object> map) {
        this.worldName = ((String) map.get("worldName"));
        this.x1 = ((Integer) map.get("x1")).intValue();
        this.x2 = ((Integer) map.get("x2")).intValue();
        this.y1 = ((Integer) map.get("y1")).intValue();
        this.y2 = ((Integer) map.get("y2")).intValue();
        this.z1 = ((Integer) map.get("z1")).intValue();
        this.z2 = ((Integer) map.get("z2")).intValue();
    }

    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap();
        map.put("worldName", this.worldName);
        map.put("x1", Integer.valueOf(this.x1));
        map.put("y1", Integer.valueOf(this.y1));
        map.put("z1", Integer.valueOf(this.z1));
        map.put("x2", Integer.valueOf(this.x2));
        map.put("y2", Integer.valueOf(this.y2));
        map.put("z2", Integer.valueOf(this.z2));
        return map;
    }

    public Location getLowerNE() {
        return new Location(getWorld(), this.x1, this.y1, this.z1);
    }

    public Location getUpperSW() {
        return new Location(getWorld(), this.x2, this.y2, this.z2);
    }

    public List<Block> getBlocks() {
        Iterator<Block> blockI = iterator();
        List<Block> copy = new ArrayList<>();
        while (blockI.hasNext()) {
            copy.add(blockI.next());
        }
        return copy;
    }

    public Location getCenter() {
        int x1 = getUpperX() + 1;
        int y1 = getUpperY() + 1;
        int z1 = getUpperZ() + 1;
        return new Location(getWorld(), getLowerX() + (x1 - getLowerX()) / 2.0D, getLowerY() + (y1 - getLowerY()) / 2.0D, getLowerZ() + (z1 - getLowerZ()) / 2.0D);
    }

    public World getWorld() {
        World world = Bukkit.getWorld(this.worldName);
        if (world == null) {
            throw new IllegalStateException("World '" + this.worldName + "' is not loaded");
        }
        return world;
    }

    public int getSizeX() {
        return this.x2 - this.x1 + 1;
    }

    public int getSizeY() {
        return this.y2 - this.y1 + 1;
    }

    public int getSizeZ() {
        return this.z2 - this.z1 + 1;
    }

    public int getLowerX() {
        return this.x1;
    }

    public int getLowerY() {
        return this.y1;
    }

    public int getLowerZ() {
        return this.z1;
    }

    public int getUpperX() {
        return this.x2;
    }

    public int getUpperY() {
        return this.y2;
    }

    public int getUpperZ() {
        return this.z2;
    }

    public Block[] corners() {
        Block[] res = new Block[8];
        World w = getWorld();
        res[0] = w.getBlockAt(this.x1, this.y1, this.z1);
        res[1] = w.getBlockAt(this.x1, this.y1, this.z2);
        res[2] = w.getBlockAt(this.x1, this.y2, this.z1);
        res[3] = w.getBlockAt(this.x1, this.y2, this.z2);
        res[4] = w.getBlockAt(this.x2, this.y1, this.z1);
        res[5] = w.getBlockAt(this.x2, this.y1, this.z2);
        res[6] = w.getBlockAt(this.x2, this.y2, this.z1);
        res[7] = w.getBlockAt(this.x2, this.y2, this.z2);
        return res;
    }

    public Cuboid expand(CuboidDirection dir, int amount) {
        switch (dir) {
            case North:
                return new Cuboid(this.worldName, this.x1 - amount, this.y1, this.z1, this.x2, this.y2, this.z2);
            case South:
                return new Cuboid(this.worldName, this.x1, this.y1, this.z1, this.x2 + amount, this.y2, this.z2);
            case East:
                return new Cuboid(this.worldName, this.x1, this.y1, this.z1 - amount, this.x2, this.y2, this.z2);
            case West:
                return new Cuboid(this.worldName, this.x1, this.y1, this.z1, this.x2, this.y2, this.z2 + amount);
            case Down:
                return new Cuboid(this.worldName, this.x1, this.y1 - amount, this.z1, this.x2, this.y2, this.z2);
            case Up:
                return new Cuboid(this.worldName, this.x1, this.y1, this.z1, this.x2, this.y2 + amount, this.z2);
        }
        throw new IllegalArgumentException("Invalid direction " + dir);
    }

    public Cuboid shift(CuboidDirection dir, int amount) {
        return expand(dir, amount).expand(dir.opposite(), -amount);
    }

    public Cuboid outset(CuboidDirection dir, int amount) {
        Cuboid c;
        switch (dir) {
            case Horizontal:
                c = expand(CuboidDirection.North, amount).expand(CuboidDirection.South, amount).expand(CuboidDirection.East, amount).expand(CuboidDirection.West, amount);
                break;
            case Vertical:
                c = expand(CuboidDirection.Down, amount).expand(CuboidDirection.Up, amount);
                break;
            case Both:
                c = outset(CuboidDirection.Horizontal, amount).outset(CuboidDirection.Vertical, amount);
                break;
            default:
                throw new IllegalArgumentException("Invalid direction " + dir);
        }
        return c;
    }

    public Cuboid inset(CuboidDirection dir, int amount) {
        return outset(dir, -amount);
    }

    public boolean contains(int x, int y, int z) {
        return (x >= this.x1) && (x <= this.x2) && (y >= this.y1) && (y <= this.y2) && (z >= this.z1) && (z <= this.z2);
    }

    public boolean contains(Block b) {
        return contains(b.getLocation());
    }

    public boolean contains(Location l) {
        if (!this.worldName.equals(l.getWorld().getName())) {
            return false;
        }
        return contains(l.getBlockX(), l.getBlockY(), l.getBlockZ());
    }

    public byte getAverageLightLevel() {
        long total = 0L;
        int n = 0;
        for (Block b : this) {
            if (b.isEmpty()) {
                total += b.getLightLevel();
                n++;
            }
        }
        return n > 0 ? (byte) (int) (total / n) : 0;
    }

    public Cuboid getFace(CuboidDirection dir) {
        switch (dir) {
            case Down:
                return new Cuboid(this.worldName, this.x1, this.y1, this.z1, this.x2, this.y1, this.z2);
            case Up:
                return new Cuboid(this.worldName, this.x1, this.y2, this.z1, this.x2, this.y2, this.z2);
            case North:
                return new Cuboid(this.worldName, this.x1, this.y1, this.z1, this.x1, this.y2, this.z2);
            case South:
                return new Cuboid(this.worldName, this.x2, this.y1, this.z1, this.x2, this.y2, this.z2);
            case East:
                return new Cuboid(this.worldName, this.x1, this.y1, this.z1, this.x2, this.y2, this.z1);
            case West:
                return new Cuboid(this.worldName, this.x1, this.y1, this.z2, this.x2, this.y2, this.z2);
        }
        throw new IllegalArgumentException("Invalid direction " + dir);
    }


    public Cuboid getBoundingCuboid(Cuboid other) {
        if (other == null) {
            return this;
        }
        int xMin = Math.min(getLowerX(), other.getLowerX());
        int yMin = Math.min(getLowerY(), other.getLowerY());
        int zMin = Math.min(getLowerZ(), other.getLowerZ());
        int xMax = Math.max(getUpperX(), other.getUpperX());
        int yMax = Math.max(getUpperY(), other.getUpperY());
        int zMax = Math.max(getUpperZ(), other.getUpperZ());

        return new Cuboid(this.worldName, xMin, yMin, zMin, xMax, yMax, zMax);
    }

    public Block getRelativeBlock(int x, int y, int z) {
        return getWorld().getBlockAt(this.x1 + x, this.y1 + y, this.z1 + z);
    }

    public Block getRelativeBlock(World w, int x, int y, int z) {
        return w.getBlockAt(this.x1 + x, this.y1 + y, this.z1 + z);
    }

    public void setXMax(int paramInt) {
        this.x2 = paramInt;
    }

    public void setXMin(int paramInt) {
        this.x1 = paramInt;
    }

    public void setYMax(int paramInt) {
        this.y2 = paramInt;
    }

    public void setYMin(int paramInt) {
        this.y1 = paramInt;
    }

    public void setZMax(int paramInt) {
        this.z2 = paramInt;
    }

    public void setZMin(int paramInt) {
        this.z1 = paramInt;
    }

    public List<Chunk> getChunks() {
        List<Chunk> res = new ArrayList();

        World w = getWorld();
        int x1 = getLowerX() & 0xFFFFFFF0;
        int x2 = getUpperX() & 0xFFFFFFF0;
        int z1 = getLowerZ() & 0xFFFFFFF0;
        int z2 = getUpperZ() & 0xFFFFFFF0;
        for (int x = x1; x <= x2; x += 16) {
            for (int z = z1; z <= z2; z += 16) {
                res.add(w.getChunkAt(x >> 4, z >> 4));
            }
        }
        return res;
    }

    public Iterator<Block> iterator() {
        return new CuboidIterator(getWorld(), this.x1, this.y1, this.z1, this.x2, this.y2, this.z2);
    }

    public Cuboid clone() {
        return new Cuboid(this);
    }

    public String toString() {
        return "Cuboid: " + this.worldName + "," + this.x1 + "," + this.y1 + "," + this.z1 + "=>" + this.x2 + "," + this.y2 + "," + this.z2;
    }

    public Cuboid transform(int i) {
        Cuboid c = clone();
        c.setYMax(c.getUpperY() + i);
        c.setXMax(c.getUpperX() + i);
        c.setXMin(c.getLowerX() - i);
        c.setYMax(c.getUpperY() + i);
        c.setYMin(c.getLowerY() - i);
        c.setZMax(c.getUpperZ() + i);
        c.setZMin(c.getLowerZ() - i);
        return c;
    }

    public boolean isInside(Location loc) {
        return (loc.getWorld().equals(getWorld())) && (loc.getX() >= getLowerX()) && (loc.getX() <= getUpperX()) &&
            (loc.getY() >= getLowerY()) && (loc.getY() <= getUpperY()) && (loc.getZ() >= getLowerZ()) && (loc.getZ() <= getUpperZ());
    }

    public boolean isInside(Location loc, int transform) {
        Cuboid c = transform(transform);
        return (loc.getWorld().equals(getWorld())) && (loc.getX() >= c.getLowerX()) && (loc.getX() <= c.getUpperX()) &&
            (loc.getY() >= c.getLowerY()) && (loc.getY() <= c.getUpperY()) && (loc.getZ() >= c.getLowerZ()) && (loc.getZ() <= c.getUpperZ());
    }

    public int getVolume() {
        Location loc1 = getLowerNE();
        Location loc2 = getUpperSW();
        return (int) ((loc2.getX() - loc1.getX() + 1.0D) * (loc2.getY() - loc1.getY() + 1.0D) * (loc2.getZ() - loc1.getZ() + 1.0D));
    }

    public List<Player> getPlayersInside() {
        ArrayList<Player> players = new ArrayList();
        for (Player all : Bukkit.getOnlinePlayers()) {
            if (isInside(all.getLocation())) {
                players.add(all);
            }
        }
        return players;
    }

    public class CuboidIterator
        implements Iterator<Block> {
        private World w;
        private int baseX;
        private int baseY;
        private int baseZ;
        private int x;
        private int y;
        private int z;
        private int sizeX;
        private int sizeY;
        private int sizeZ;

        public CuboidIterator(World w, int x1, int y1, int z1, int x2, int y2, int z2) {
            this.w = w;
            this.baseX = x1;
            this.baseY = y1;
            this.baseZ = z1;
            this.sizeX = (Math.abs(x2 - x1) + 1);
            this.sizeY = (Math.abs(y2 - y1) + 1);
            this.sizeZ = (Math.abs(z2 - z1) + 1);
            this.x = (this.y = this.z = 0);
        }

        public boolean hasNext() {
            return (this.x < this.sizeX) && (this.y < this.sizeY) && (this.z < this.sizeZ);
        }

        public Block next() {
            Block b = this.w.getBlockAt(this.baseX + this.x, this.baseY + this.y, this.baseZ + this.z);
            if (++this.x >= this.sizeX) {
                this.x = 0;
                if (++this.y >= this.sizeY) {
                    this.y = 0;
                    this.z += 1;
                }
            }
            return b;
        }

        public void remove() {
        }
    }

    public static enum CuboidDirection {
        North, East, South, West, Up, Down, Horizontal, Vertical, Both, Unknown;

        public CuboidDirection opposite() {
            switch (this) {
                case North:
                    return South;
                case East:
                    return West;
                case South:
                    return North;
                case West:
                    return East;
                case Horizontal:
                    return Vertical;
                case Vertical:
                    return Horizontal;
                case Up:
                    return Down;
                case Down:
                    return Up;
                case Both:
                    return Both;
                default:
                    return Unknown;
            }
        }

    }
}
