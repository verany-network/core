package net.verany.api.player.permission.group;

import lombok.Getter;
import org.bukkit.ChatColor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class PlaytimeGroup extends AbstractPermissionGroup {

    public static final AbstractPermissionGroup CHAMPION = new PlaytimeGroup("Champion-1", ChatColor.LIGHT_PURPLE, "a", Long.MAX_VALUE);
    public static final AbstractPermissionGroup DIAMOND = new PlaytimeGroup("Diamond-1", ChatColor.DARK_PURPLE, "b", 4400);
    public static final AbstractPermissionGroup PLATINUM_1 = new PlaytimeGroup("Platinum-1", ChatColor.AQUA, "c", 4000);
    public static final AbstractPermissionGroup PLATINUM_2 = new PlaytimeGroup("Platinum-2", ChatColor.AQUA, "d", 3600);
    public static final AbstractPermissionGroup PLATINUM_3 = new PlaytimeGroup("Platinum-3", ChatColor.AQUA, "e", 3200);
    public static final AbstractPermissionGroup GOLD_1 = new PlaytimeGroup("Gold-1", ChatColor.YELLOW, "f", 3000);
    public static final AbstractPermissionGroup GOLD_2 = new PlaytimeGroup("Gold-2", ChatColor.YELLOW, "g", 2800);
    public static final AbstractPermissionGroup GOLD_3 = new PlaytimeGroup("Gold-3", ChatColor.YELLOW, "h", 2600);
    public static final AbstractPermissionGroup SILVER_1 = new PlaytimeGroup("Silver-1", ChatColor.GRAY, "i", 2500);
    public static final AbstractPermissionGroup SILVER_2 = new PlaytimeGroup("Silver-2", ChatColor.GRAY, "j", 2400);
    public static final AbstractPermissionGroup SILVER_3 = new PlaytimeGroup("Silver-3", ChatColor.GRAY, "k", 2300);
    public static final AbstractPermissionGroup SILVER_4 = new PlaytimeGroup("Silver-4", ChatColor.GRAY, "l", 2200);
    public static final AbstractPermissionGroup SILVER_5 = new PlaytimeGroup("Silver-5", ChatColor.GRAY, "m", 2100);
    public static final AbstractPermissionGroup BRONZE_1 = new PlaytimeGroup("Bronze-1", ChatColor.GOLD, "n", 2000);
    public static final AbstractPermissionGroup BRONZE_2 = new PlaytimeGroup("Bronze-2", ChatColor.GOLD, "o", 1900);
    public static final AbstractPermissionGroup BRONZE_3 = new PlaytimeGroup("Bronze-3", ChatColor.GOLD, "p", 1800);
    public static final AbstractPermissionGroup BRONZE_4 = new PlaytimeGroup("Bronze-4", ChatColor.GOLD, "q", 1700);
    public static final AbstractPermissionGroup BRONZE_5 = new PlaytimeGroup("Bronze-5", ChatColor.GOLD, "r", 1600);
    public static final AbstractPermissionGroup COPPER_1 = new PlaytimeGroup("Copper-1", ChatColor.RED, "s", 1500);
    public static final AbstractPermissionGroup COPPER_2 = new PlaytimeGroup("Copper-2", ChatColor.RED, "t", 1400);
    public static final AbstractPermissionGroup COPPER_3 = new PlaytimeGroup("Copper-3", ChatColor.RED, "u", 1300);
    public static final AbstractPermissionGroup COPPER_4 = new PlaytimeGroup("Copper-4", ChatColor.RED, "v", 1200);
    public static final AbstractPermissionGroup COPPER_5 = new PlaytimeGroup("Copper-5", ChatColor.RED, "w", 1100);
    public static final AbstractPermissionGroup UNRANKED = new PlaytimeGroup("Unranked", ChatColor.GRAY, "x", 0);

    private final long playPoints;

    public PlaytimeGroup(String name, ChatColor color, String scoreboardId, long playPoints) {
        super(name, color, scoreboardId, "", 0);
        this.playPoints = playPoints + (playPoints / 3);
        PermissionGroup.VALUES.add(this);
    }

    public int getId() {
        if (!getName().contains("-")) return -1;
        try {
            return Integer.parseInt(getName().split("-")[1]);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public static AbstractPermissionGroup getGroupByTime(long time) {
        List<AbstractPermissionGroup> list = PermissionGroup.VALUES;
        List<AbstractPermissionGroup> values = list.stream().filter(abstractPermissionGroup -> abstractPermissionGroup instanceof PlaytimeGroup).collect(Collectors.toList());
        for (int i = 0; i < values.size(); i++) {
            PlaytimeGroup group = (PlaytimeGroup) values.get(i);
            if (i != 0) {
                PlaytimeGroup next = (PlaytimeGroup) values.get(i - 1);
                if (group.getPlayPoints() < time && next.getPlayPoints() >= time)
                    return group;
            }
        }
        return UNRANKED;
    }

}
