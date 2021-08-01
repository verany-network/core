package net.verany.api.tablist;

import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public interface ITablistObject {

    Team getTeamForPlayer(Player player, Scoreboard scoreboard);

    void setTabList(Player player);

}
