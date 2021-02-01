package net.verany.executor.commands;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.verany.api.Verany;
import net.verany.api.placeholder.Placeholder;
import net.verany.api.player.IPlayerInfo;
import net.verany.api.player.permission.IPermissionObject;
import net.verany.api.player.permission.duration.AbstractGroupTime;
import net.verany.api.player.permission.duration.AbstractGroupTime.GroupDuration;
import net.verany.api.player.permission.duration.GroupTime;
import net.verany.api.player.permission.group.AbstractPermissionGroup;
import net.verany.api.player.permission.group.PermissionGroup;
import net.verany.api.tablist.TabListObject;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RankCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        Player player = (Player) commandSender;
        IPlayerInfo playerInfo = Verany.PROFILE_OBJECT.getPlayer(player.getUniqueId()).get();

        if (!player.hasPermission("verany.command.rank")) {
            return false;
        }

        if (strings.length == 4) {
            if (strings[0].equalsIgnoreCase("set")) {
                Verany.PROFILE_OBJECT.getPlayer(strings[1]).ifPresentOrElse(iPlayerInfo -> {
                    AbstractPermissionGroup permissionGroup = PermissionGroup.getGroupByName(strings[2]);
                    if (permissionGroup == null) {
                        playerInfo.sendKey(playerInfo.getPrefix("RankSystem"), "core.rank.not_found", new Placeholder("%rank%", strings[2]));
                        return;
                    }
                    if (iPlayerInfo.getPermissionObject().hasPermission("*")) {
                        playerInfo.sendKey(playerInfo.getPrefix("RankSystem"), "core.not.allowed", new Placeholder("%player%", iPlayerInfo.getNameWithColor()));
                        return;
                    }
                    if (iPlayerInfo.getName().equals(player.getName())) {
                        playerInfo.sendKey(playerInfo.getPrefix("RankSystem"), "core.rank.cant_change_self");
                        return;
                    }
                    GroupDuration time;
                    if (strings[3].split("=")[0].equalsIgnoreCase("h")) {
                        int amount = Integer.parseInt(strings[3].split("=")[1]);
                        time = GroupTime.HOURS.of(amount);
                    } else if (strings[3].split("=")[0].equalsIgnoreCase("d")) {
                        int amount = Integer.parseInt(strings[3].split("=")[1]);
                        time = GroupTime.DAYS.of(amount);
                    } else
                        time = GroupTime.LIFETIME;
                    if (iPlayerInfo.getPermissionObject().getCurrentGroup().getGroup().getName().equals(permissionGroup.getName()) && iPlayerInfo.getPermissionObject().getCurrentGroup().getDuration().getMillis() == time.getMillis()) {
                        playerInfo.sendKey(playerInfo.getPrefix("RankSystem"), "core.rank.already_in_group", new Placeholder("%player%", iPlayerInfo.getNameWithColor()), new Placeholder("%rank%", permissionGroup.getColor() + permissionGroup.getName()), new Placeholder("%time%", strings[3]));
                        return;
                    }
                    playerInfo.sendKey(playerInfo.getPrefix("RankSystem"), "core.rank.updated", new Placeholder("%player%", iPlayerInfo.getNameWithColor()), new Placeholder("%rank%", permissionGroup.getColor().toString() + permissionGroup.getName()), new Placeholder("%time%", strings[3]));
                    iPlayerInfo.getPermissionObject().setGroup(new IPermissionObject.GroupData(permissionGroup, time));
                    Bukkit.getOnlinePlayers().forEach(onlinePlayer -> new TabListObject().setTabList(onlinePlayer));
                }, () -> {
                    playerInfo.sendKey(playerInfo.getPrefix("RankSystem"), "core.rank.player_not_found", new Placeholder("%player%", strings[1]));
                });
                return false;
            }
        } else if (strings.length == 2) {
            if (strings[0].equalsIgnoreCase("info")) {
                Verany.PROFILE_OBJECT.getPlayer(strings[1]).ifPresentOrElse(iPlayerInfo -> {
                    IPermissionObject permissionObject = iPlayerInfo.getPermissionObject();
                    player.sendMessage(playerInfo.getKeyArray("core.rank.info", "~", new Placeholder("%player%", iPlayerInfo.getNameWithColor()), new Placeholder("%prefix%", playerInfo.getPrefix("RankSystem")), new Placeholder("%rank%", permissionObject.getCurrentGroup().getGroup().getColor() + permissionObject.getCurrentGroup().getGroup().getName()), new Placeholder("%time%", permissionObject.getCurrentGroup().getGroup().getColor()), new Placeholder("%group_since%", new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date(permissionObject.getCurrentGroup().getTimestamp())))));
                }, () -> {
                    playerInfo.sendKey(playerInfo.getPrefix("RankSystem"), "core.rank.player_not_found", new Placeholder("%player%", strings[1]));
                });
                return false;
            }
        }

        player.sendMessage(playerInfo.getKeyArray("core.rank.help", "~", new Placeholder("%prefix%", playerInfo.getPrefix("RankSystem"))));

        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        Player player = (Player) commandSender;
        if (!player.hasPermission("verany.command.rank"))
            return ImmutableList.of();

        List<String> arguments = Lists.newArrayList("set");

        List<String> players = new ArrayList<>();
        for (IPlayerInfo playerInfo : Verany.PROFILE_OBJECT.getRegisteredPlayers())
            players.add(playerInfo.getName());

        List<String> permissionGroups = new ArrayList<>();
        for (AbstractPermissionGroup value : PermissionGroup.VALUES)
            permissionGroups.add(value.getName());

        List<String> times = Lists.newArrayList("h=", "d=", "lifetime");

        if (strings.length == 1)
            return StringUtil.copyPartialMatches(strings[0], arguments, new ArrayList<>(arguments.size()));
        else if (strings.length == 2)
            return StringUtil.copyPartialMatches(strings[1], players, new ArrayList<>(players.size()));
        else if (strings.length == 3)
            return StringUtil.copyPartialMatches(strings[2], permissionGroups, new ArrayList<>(permissionGroups.size()));
        else if (strings.length == 4)
            return StringUtil.copyPartialMatches(strings[3], times, new ArrayList<>(times.size()));
        return ImmutableList.of();
    }
}
