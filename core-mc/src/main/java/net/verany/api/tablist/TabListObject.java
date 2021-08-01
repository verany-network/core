package net.verany.api.tablist;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.verany.api.Verany;
import net.verany.api.config.IngameConfig;
import net.verany.api.player.IPlayerInfo;
import net.verany.api.player.permission.IPermissionObject;
import net.verany.volcano.player.IVolcanoPlayer;
import net.verany.volcano.round.GameSetting;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.text.MessageFormat;

public class TabListObject implements ITablistObject {

    @Override
    public Team getTeamForPlayer(Player player, Scoreboard scoreboard) {
        IPermissionObject permissionObject = Verany.getPlayer(player).getPermissionObject();
        return scoreboard.getTeam(permissionObject.getCurrentGroup().getGroup().getScoreboardId() + player.getUniqueId().toString().substring(0, 10));
    }

    @Override
    public void setTabList(Player player) {
        if (Verany.getPlayer(player.getUniqueId()).getPlayer(IVolcanoPlayer.class) != null && !Verany.getPlayer(player.getUniqueId()).getPlayer(IVolcanoPlayer.class).getRound().getSettingValue(GameSetting.TAB_LIST))
            return;
        Scoreboard scoreboard = player.getScoreboard();
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            IPermissionObject permissionObject = Verany.PROFILE_OBJECT.getPlayer(onlinePlayer.getUniqueId(), IPlayerInfo.class).get().getPermissionObject();
            if (scoreboard.getTeam(permissionObject.getCurrentGroup().getGroup().getScoreboardId() + onlinePlayer.getUniqueId().toString().substring(0, 10)) == null) {
                Team team = scoreboard.registerNewTeam(permissionObject.getCurrentGroup().getGroup().getScoreboardId() + onlinePlayer.getUniqueId().toString().substring(0, 10));
                team.prefix(Component.text(MessageFormat.format(IngameConfig.TAB_LIST_FORMAT.getValue(), ChatColor.valueOf(permissionObject.getCurrentGroup().getGroup().getColor()), permissionObject.getCurrentGroup().getGroup().getPrefix())));
                team.color(NamedTextColor.GRAY);
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
