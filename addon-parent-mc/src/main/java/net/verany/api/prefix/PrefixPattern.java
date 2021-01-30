package net.verany.api.prefix;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class PrefixPattern extends AbstractPrefixPattern {

    public static final List<AbstractPrefixPattern> VALUES = new ArrayList<>();

    public static final AbstractPrefixPattern DEFAULT = new PrefixPattern("DEFAULT", " §8◗§7◗ §b§l{0}§3§l{1} §8▏ §r", new PrefixColor(ChatColor.AQUA, ChatColor.DARK_AQUA));
    public static final AbstractPrefixPattern DEFAULT_GREEN = new PrefixPattern("DEFAULT_GREEN", " §8◗§7◗ §a§l{0}§2§l{1} §8▏ §r", new PrefixColor(ChatColor.GREEN, ChatColor.DARK_GREEN));
    public static final AbstractPrefixPattern DEFAULT_ORANGE = new PrefixPattern("DEFAULT_ORANGE", " §8◗§7◗ §e§l{0}§6§l{1} §8▏ §r", new PrefixColor(ChatColor.YELLOW, ChatColor.GOLD));
    public static final AbstractPrefixPattern BRACKETS = new PrefixPattern("BRACKETS", "§8(§b{0}§3{1}§8) §r", new PrefixColor(ChatColor.AQUA, ChatColor.DARK_AQUA));
    public static final AbstractPrefixPattern CLEAN = new PrefixPattern("CLEAN", "§8[§f{0}{1}§8] §r", new PrefixColor(ChatColor.WHITE, ChatColor.WHITE));

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
        return null;
    }

    public static void load() {}

}
