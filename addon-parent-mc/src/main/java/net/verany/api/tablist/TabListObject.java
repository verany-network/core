package net.verany.api.tablist;

import net.verany.api.Verany;
import net.verany.api.config.IngameConfig;
import net.verany.api.player.IPlayerInfo;
import net.verany.api.player.permission.IPermissionObject;
import net.verany.api.player.permission.group.AbstractPermissionGroup;
import net.verany.api.player.permission.group.PermissionGroup;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class TabListObject implements ITablistObject {

    @Override
    public Team getTeamForPlayer(Player player, Scoreboard scoreboard) {
        IPermissionObject permissionObject = Verany.PROFILE_OBJECT.getPlayer(player.getUniqueId()).get().getPermissionObject();
        return scoreboard.getTeam(permissionObject.getCurrentGroup().getGroup().getScoreboardId() + player.getUniqueId().toString().substring(0, 10));
    }

    @Override
    public void setTabList(Player player) {
        if (!IngameConfig.TAB_LIST.getValue()) return;
        Scoreboard scoreboard = player.getScoreboard();
        /*for (AbstractPermissionGroup value : PermissionGroup.VALUES) {
            if (scoreboard.getTeam(value.getScoreboardId() ) == null) {
                Team teams = scoreboard.registerNewTeam(value.getScoreboardId());
                teams.setPrefix(Verany.format(IngameConfig.TAB_LIST_FORMAT.getValue(), value.getColor(), value.getName()));
                teams.setColor(value.getColor());
                teams.setOption(Team.Option.COLLISION_RULE, IngameConfig.PLAYER_COLLISION.getValue() ? Team.OptionStatus.ALWAYS : Team.OptionStatus.NEVER);
            }
        }*/
        String s = "hallo";
        s = s.substring(s.length() - 2);
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            IPermissionObject permissionObject = Verany.PROFILE_OBJECT.getPlayer(onlinePlayer.getUniqueId()).get().getPermissionObject();
            if (scoreboard.getTeam(permissionObject.getCurrentGroup().getGroup().getScoreboardId() + onlinePlayer.getUniqueId().toString().substring(0, 10)) == null) {
                Team team = scoreboard.registerNewTeam(permissionObject.getCurrentGroup().getGroup().getScoreboardId() + onlinePlayer.getUniqueId().toString().substring(0, 10));
                team.setPrefix(Verany.format(IngameConfig.TAB_LIST_FORMAT.getValue(), permissionObject.getCurrentGroup().getGroup().getColor(), permissionObject.getCurrentGroup().getGroup().getPrefix()));
                team.setColor(ChatColor.GRAY);
                team.setOption(Team.Option.COLLISION_RULE, IngameConfig.PLAYER_COLLISION.getValue() ? Team.OptionStatus.ALWAYS : Team.OptionStatus.NEVER);
            }
        }

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            Team playerTeam = this.getTeamForPlayer(onlinePlayer, scoreboard);
            if (playerTeam != null && !playerTeam.hasEntry(onlinePlayer.getName()))
                playerTeam.addEntry(onlinePlayer.getName());

            if (!IngameConfig.TAB_LIST_CLAN.getValue()) continue;
        }
    }
}
