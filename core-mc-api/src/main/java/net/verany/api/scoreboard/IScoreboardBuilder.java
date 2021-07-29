package net.verany.api.scoreboard;

import org.bukkit.scoreboard.Scoreboard;

import java.util.List;
import java.util.Map;

public interface IScoreboardBuilder {

    /**
     * @param title display name title
     */
    void setTitle(String title);

    /**
     * @param slot score id
     * @param text score text
     */
    void setSlot(int slot, String text);

    /**
     * @param slot score id
     */
    void removeSlot(int slot);

    /**
     * @param list scoreboard from list
     */
    void setSlotsFromList(List<String> list);

    /**
     * @param list scoreboard from string array
     */
    void setSlotsFromArray(String... list);

    /**
     *
     * @param map scoreboard from map
     */
    void setSlotsFromMap(Map<Integer, String> map);

    /**
     * @return returns the bukkit scoreboard
     */
    Scoreboard getScoreboard();

}
