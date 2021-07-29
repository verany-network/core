package net.verany.api.npc.animation;

public enum NPCAnimation {

    SWING_MAIN_HAND(0),
    TAKE_DAMAGE(1),
    LEAVE_BED(2),
    SWING_OFF_HAND(3),
    CRITICAL_EFFECT(4),
    MAGIC_CRITIAL_EFFECT(5);

    private final int id;

    private NPCAnimation(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }
}
