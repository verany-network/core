package net.verany.api.player.leveling;

public class LevelCalculator {

    public static int expForLevel(int level) {
        double baseExp = 100;
        double exponent = 1.04F;
        return (int) (baseExp + (baseExp * Math.pow(level, exponent)));
    }

    public static int fullTargetExp(int level) {
        double requiredExp = 0;
        for (int i = 0; i <= level; i++)
            requiredExp += expForLevel(i);
        return (int) requiredExp;
    }

    public static int level(int exp) {
        int level = 0;
        double maxExp = expForLevel(0);
        do {
            maxExp += expForLevel(++level);
        } while (maxExp < exp);
        return level;
    }

}
