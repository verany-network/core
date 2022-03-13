package net.verany.api.prefix;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class PrefixPattern extends AbstractPrefixPattern {

    public static final List<AbstractPrefixPattern> VALUES = new ArrayList<>();

    public static final AbstractPrefixPattern BLUE = new PrefixPattern("BLUE", " §8◗§7◗ §b§l{0}§3§l{1} §8▏ §r", new PrefixColor("AQUA", "DARK_AQUA"));
    public static final AbstractPrefixPattern WHITE = new PrefixPattern("WHITE", " §8◗§7◗ §f§l{0}{1} §8▏ §r", new PrefixColor("WHITE", "WHITE"));
    public static final AbstractPrefixPattern GREEN = new PrefixPattern("GREEN", " §8◗§7◗ §a§l{0}§2§l{1} §8▏ §r", new PrefixColor("GREEN", "DARK_GREEN"));
    public static final AbstractPrefixPattern YELLOW = new PrefixPattern("YELLOW", " §8◗§7◗ §e§l{0}§6§l{1} §8▏ §r", new PrefixColor("YELLOW", "GOLD"));
    public static final AbstractPrefixPattern RED = new PrefixPattern("RED", " §8◗§7◗ §c§l{0}§4§l{1} §8▏ §r", new PrefixColor("RED", "DARK_RED"));
    public static final AbstractPrefixPattern CLEAN_BLUE = new PrefixPattern("CLEAN_BLUE", "§8[§b§l{0}§3§l{1}§8] §r", new PrefixColor("AQUA", "DARK_AQUA"));
    public static final AbstractPrefixPattern CLEAN_WHITE = new PrefixPattern("CLEAN_WHITE", "§8[§f§l{0}{1}§8] §r", new PrefixColor("WHITE", "WHITE"));
    public static final AbstractPrefixPattern CLEAN_GREEN = new PrefixPattern("CLEAN_GREEN", "§8[§a§l{0}§2§l{1}§8] §r", new PrefixColor("GREEN", "DARK_GREEN"));
    public static final AbstractPrefixPattern CLEAN_YELLOW = new PrefixPattern("CLEAN_YELLOW", "§8[§e§l{0}§6§l{1}§8] §r", new PrefixColor("YELLOW", "GOLD"));
    public static final AbstractPrefixPattern CLEAN_RED = new PrefixPattern("CLEAN_RED", "§8[§c§l{0}§4§l{1}§8] §r", new PrefixColor("RED", "DARK_RED"));

    public PrefixPattern(String key, String pattern, PrefixColor color) {
        super(key, pattern, color);
        VALUES.add(this);
    }

    public PrefixPattern(String pattern, PrefixColor color) {
        super("of", pattern, color);
    }

    public static AbstractPrefixPattern of(String pattern, PrefixColor color) {
        return new PrefixPattern(pattern, color);
    }

    @Override
    public String getExample() {
        return MessageFormat.format(getPattern(), "Test", "Text");
    }

    public static AbstractPrefixPattern getPatternByKey(String key) {
        for (AbstractPrefixPattern value : VALUES)
            if (value.getKey().equalsIgnoreCase(key))
                return value;
        return BLUE;
    }

    public static void load() {
    }

}
