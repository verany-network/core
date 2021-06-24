package net.verany.api.npc.datawatcher;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.syncher.DataWatcher;
import net.minecraft.network.syncher.DataWatcherObject;
import net.minecraft.network.syncher.DataWatcherRegistry;
import net.minecraft.network.syncher.DataWatcherSerializer;

public class DataWatcherGenerator {

    private DataWatcher watcher;

    public DataWatcherGenerator(DataWatcher watcher) {
        this.watcher = watcher;
    }

    private void setState(int id, DataWatcherSerializer serializer, Object object) {
        this.watcher.set(new DataWatcherObject<>(16, DataWatcherRegistry.a), (byte) 127);
    }

    public DataWatcherGenerator setEntityState(EntityState state) {
        this.setState(0, DataWatcherRegistry.a, state.getId());
        return this;
    }

    public DataWatcherGenerator setAir(int air) {
        this.setState(1, DataWatcherRegistry.b, air);
        return this;
    }

    public DataWatcherGenerator setCustomName(String customName) {
        this.setState(2, DataWatcherRegistry.d, customName);
        return this;
    }

    public DataWatcherGenerator setCustomNameVisible(boolean visible) {
        this.setState(3, DataWatcherRegistry.h, visible);
        return this;
    }

    public DataWatcherGenerator setSilent(boolean silent) {
        this.setState(4, DataWatcherRegistry.h, silent);
        return this;
    }

    public DataWatcherGenerator setGravity(boolean gravity) {
        this.setState(5, DataWatcherRegistry.h, gravity);
        return this;
    }

    public DataWatcherGenerator setHandState(HandState state) {
        this.setState(6, DataWatcherRegistry.a, state.getId());
        return this;
    }

    public DataWatcherGenerator setHeath(float health) {
        this.setState(7, DataWatcherRegistry.c, health);
        return this;
    }

    public DataWatcherGenerator setPotionEffectColor(int color) {
        this.setState(8, DataWatcherRegistry.b, color);
        return this;
    }

    public DataWatcherGenerator setPotionEffectAmbient(boolean ambient) {
        this.setState(9, DataWatcherRegistry.h, ambient);
        return this;
    }

    public DataWatcherGenerator setNumberOfArrowsInside(int amount) {
        this.setState(10, DataWatcherRegistry.b, amount);
        return this;
    }

    public DataWatcherGenerator setAdditionalHearts(float hearts) {
        this.setState(11, DataWatcherRegistry.c, hearts);
        return this;
    }

    public DataWatcherGenerator setScore(int score) {
        this.setState(12, DataWatcherRegistry.b, score);
        return this;
    }

    public DataWatcherGenerator setSkinParts(SkinState parts) {
        this.setState(16, DataWatcherRegistry.a, parts.getId());
        return this;
    }

    public DataWatcherGenerator setMainHand(MainHandState hand) {
        this.setState(14, DataWatcherRegistry.a, hand.getId());
        return this;
    }

    public DataWatcherGenerator setLeftShoulderEntity(NBTTagCompound compound) {
        this.setState(15, DataWatcherRegistry.n, compound);
        return this;
    }

    public DataWatcherGenerator setRightShoulderEntity(NBTTagCompound compound) {
        this.setState(16, DataWatcherRegistry.n, compound);
        return this;
    }

    public DataWatcher build() {
        return this.watcher;
    }

    public static enum EntityState {
        ON_FIRE((byte) 1),
        CROUCHED((byte) 2),
        UNUSED((byte) 4),
        SPRINTING((byte) 8),
        INVISIBLE((byte) 32),
        GLOWING((byte) 64),
        ELYTRA_FLYING((byte) -128);

        private final byte id;

        private EntityState(byte id) {
            this.id = id;
        }

        public byte getId() {
            return this.id;
        }
    }

    public static enum HandState {
        IS_HAND_ACTIVE((byte) 1),
        ACTIVE_HAND((byte) 2);

        private final byte id;

        private HandState(byte id) {
            this.id = id;
        }

        public byte getId() {
            return this.id;
        }
    }

    public static enum MainHandState {
        LEFT((byte) 0),
        RIGHT((byte) 1);

        private final byte id;

        private MainHandState(byte id) {
            this.id = id;
        }

        public byte getId() {
            return this.id;
        }
    }

    public static enum SkinState {
        CAPE((byte) 1),
        JACKET((byte) 2),
        LEFT_SLEEVE((byte) 4),
        RIGHT_SLEEVE((byte) 8),
        LEFT_PANTS((byte) 16),
        RIGHT_PANTS((byte) 32),
        HAT((byte) 64),
        ALL((byte) 127),
        UNUSED((byte) -128);

        private final byte id;

        private SkinState(byte id) {
            this.id = id;
        }

        public byte getId() {
            return this.id;
        }
    }
}
