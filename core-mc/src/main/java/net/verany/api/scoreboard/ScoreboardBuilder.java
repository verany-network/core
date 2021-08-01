package net.verany.api.scoreboard;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ScoreboardBuilder implements IScoreboardBuilder {

    private final Scoreboard scoreboard;
    private final Objective objective;

    public ScoreboardBuilder(Player player) {
        this.scoreboard = player.getScoreboard();
        this.objective = player.getScoreboard().getObjective(DisplaySlot.SIDEBAR) == null ? player.getScoreboard().registerNewObjective("aaa", "bbb") : player.getScoreboard().getObjective(DisplaySlot.SIDEBAR);
        this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        for (int i = 0; i <= 15; i++) {
            Team team = scoreboard.getTeam("slot_" + i) == null ? scoreboard.registerNewTeam("slot_" + i) : scoreboard.getTeam("slot_" + i);
            team.addEntry(genEntry(i));
        }
    }

    @Override
    public void setTitle(String title) {
        objective.setDisplayName(title.length() > 32 ? title.substring(0, 32) : title);
    }

    @Override
    public void setSlot(int slot, String text) {
        Team team = scoreboard.getTeam("slot_" + slot);
        String entry = genEntry(slot);
        if (!scoreboard.getEntries().contains(entry))
            objective.getScore(entry).setScore(slot);

        String prefix = getFirstSplit(text);
        String suffix = getFirstSplit(ChatColor.getLastColors(prefix) + getSecondSplit(text));
        team.setPrefix(prefix);
        team.setSuffix(suffix);
    }

    @Override
    public void removeSlot(int slot) {
        String entry = genEntry(slot);
        if (scoreboard.getEntries().contains(entry))
            scoreboard.resetScores(entry);
    }

    @Override
    public void setSlotsFromList(List<String> list) {
        while (list.size() > 15)
            list.remove(list.size() - 1);

        int slot = list.size();

        if(slot < 15)
            for (int i = (slot + 1); i <= 15; i++)
                removeSlot(i);

        for (String s : list) {
            setSlot(slot, s);
            slot--;
        }
    }

    @Override
    public void setSlotsFromArray(String... list) {
        setSlotsFromList(Arrays.asList(list));
    }

    @Override
    public void setSlotsFromMap(Map<Integer, String> map) {
        List<String> list = new ArrayList<>();
        map.forEach(list::add);
        setSlotsFromList(list);
    }

    private String genEntry(int slot) {
        return ChatColor.values()[slot].toString();
    }

    private String getFirstSplit(String s) {
        return s.length() > 16 ? s.substring(0, 16) : s;
    }

    private String getSecondSplit(String s) {
        if (s.length() > 32)
            s = s.substring(0, 32);
        return s.length() > 16 ? s.substring(16) : "";
    }

    @Override
    public Scoreboard getScoreboard() {
        return scoreboard;
    }
}
