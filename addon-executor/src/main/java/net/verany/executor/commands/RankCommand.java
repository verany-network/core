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
import net.verany.executor.CoreExecutor;
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
import java.util.*;

public class RankCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        Player player = (Player) commandSender;
        IPlayerInfo playerInfo = Verany.PROFILE_OBJECT.getPlayer(player.getUniqueId()).get();

        if (!player.hasPermission("verany.command.rank")) {
            return false;
        }

        if (strings.length == 2) {
            if (strings[0].equalsIgnoreCase("info")) {
                AbstractPermissionGroup permissionGroup = PermissionGroup.getGroupByName(strings[1].replace("-", " "));
                if (permissionGroup != null) {
                    String rank = permissionGroup.getColor() + permissionGroup.getName();
                    String permissions = permissionGroup.getPermissions().toString();
                    String children = permissionGroup.getStringChildren().toString();
                    String prefix = playerInfo.getPrefix("RankSystem");
                    int playersInGroup = permissionGroup.getPlayersInGroup().size();
                    player.sendMessage(playerInfo.getKeyArray("core.rank.info.group", '~', new Placeholder("%prefix%", prefix), new Placeholder("%rank%", rank), new Placeholder("%permissions%", permissions), new Placeholder("%children%", children), new Placeholder("%players_in_group%", Verany.asDecimal(playersInGroup))));
                    return false;
                }
                Verany.PROFILE_OBJECT.getPlayer(strings[1]).ifPresentOrElse(iPlayerInfo -> {
                    IPermissionObject permissionObject = iPlayerInfo.getPermissionObject();
                    String playerName = iPlayerInfo.getNameWithColor();
                    String rank = permissionObject.getCurrentGroup().getGroup().getColor() + permissionObject.getCurrentGroup().getGroup().getName();
                    String prefix = playerInfo.getPrefix("RankSystem");
                    String time;
                    if (permissionObject.getCurrentGroup().getDuration().getMillis() == GroupTime.LIFETIME.getMillis())
                        time = "Lifetime";
                    else
                        time = permissionObject.getCurrentGroup().getDuration().getAmount() + " " + permissionObject.getCurrentGroup().getDuration().getKey() + " - Expires: " + Verany.getPrettyTime(Locale.ENGLISH, System.currentTimeMillis() + permissionObject.getCurrentGroup().getTimeLeft());
                    String groupSince = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date(permissionObject.getCurrentGroup().getTimestamp()));
                    String permissions = permissionObject.getPermissions().toString();
                    player.sendMessage(playerInfo.getKeyArray("core.rank.info", '~', new Placeholder("%player%", playerName), new Placeholder("%prefix%", prefix), new Placeholder("%rank%", rank), new Placeholder("%time%", time), new Placeholder("%group_since%", groupSince), new Placeholder("%permissions%", permissions)));
                }, () -> {
                    playerInfo.sendKey(playerInfo.getPrefix("RankSystem"), "core.rank.player_not_found", new Placeholder("%player%", strings[1]));
                });
                return false;
            }
        } else if (strings.length == 4) {
            if (strings[0].equalsIgnoreCase("set")) {
                Verany.PROFILE_OBJECT.getPlayer(strings[1]).ifPresentOrElse(iPlayerInfo -> {
                    AbstractPermissionGroup permissionGroup = PermissionGroup.getGroupByName(strings[2].replace("-", " "));
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
                    try {
                        if (strings[3].split("=")[0].equalsIgnoreCase("h")) {
                            int amount = Integer.parseInt(strings[3].split("=")[1]);
                            time = GroupTime.HOURS.of(amount);
                        } else if (strings[3].split("=")[0].equalsIgnoreCase("d")) {
                            int amount = Integer.parseInt(strings[3].split("=")[1]);
                            time = GroupTime.DAYS.of(amount);
                        } else
                            time = GroupTime.LIFETIME;
                    } catch (NumberFormatException e) {
                        playerInfo.sendKey(playerInfo.getPrefix("RankSystem"), "core.rank.not_a_number", new Placeholder("%argument%", strings[3].split("=")[1]));
                        return;
                    }
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
            } else if (strings[0].equalsIgnoreCase("permission")) {
                AbstractPermissionGroup permissionGroup = PermissionGroup.getGroupByName(strings[1].replace("-", " "));
                if (permissionGroup != null) {
                    String permission = strings[3];
                    if (!playerInfo.getPermissionObject().getCurrentGroup().getGroup().getName().equals(PermissionGroup.ADMINISTRATOR.getName()) && (permissionGroup.getName().equals(PermissionGroup.ADMINISTRATOR.getName()) || permissionGroup.getPermissions().contains("*") || permission.contains("*"))) {
                        playerInfo.sendKey(playerInfo.getPrefix("RankSystem"), "core.rank.not.allowed", new Placeholder("%group%", permissionGroup.getColor() + permissionGroup.getName()));
                        return false;
                    }
                    if (strings[2].equalsIgnoreCase("add")) {
                        if (permissionGroup.getPermissions().contains(permission)) {
                            playerInfo.sendKey(playerInfo.getPrefix("RankSystem"), "core.group.already_has_permission", new Placeholder("%group%", permissionGroup.getColor() + permissionGroup.getName()), new Placeholder("%permission%", permission));
                            return false;
                        }
                        permissionGroup.getPermissions().add(permission);
                        Verany.updatePermissionGroup(CoreExecutor.INSTANCE, permissionGroup);
                        playerInfo.sendKey(playerInfo.getPrefix("RankSystem"), "core.group.added.permission", new Placeholder("%group%", permissionGroup.getColor() + permissionGroup.getName()), new Placeholder("%permission%", permission));
                    } else if (strings[2].equalsIgnoreCase("remove")) {
                        if (!permissionGroup.getPermissions().contains(permission)) {
                            playerInfo.sendKey(playerInfo.getPrefix("RankSystem"), "core.group.does_not_has_permission", new Placeholder("%group%", permissionGroup.getColor() + permissionGroup.getName()), new Placeholder("%permission%", permission));
                            return false;
                        }
                        permissionGroup.getPermissions().remove(permission);
                        Verany.updatePermissionGroup(CoreExecutor.INSTANCE, permissionGroup);
                        playerInfo.sendKey(playerInfo.getPrefix("RankSystem"), "core.group.removed.permission", new Placeholder("%group%", permissionGroup.getColor() + permissionGroup.getName()), new Placeholder("%permission%", permission));
                    }
                    return false;
                }
                Verany.PROFILE_OBJECT.getPlayer(strings[1]).ifPresentOrElse(iPlayerInfo -> {
                    if (iPlayerInfo.getName().equals(player.getName())) {
                        playerInfo.sendKey(playerInfo.getPrefix("RankSystem"), "core.rank.cant_change_self");
                        return;
                    }
                    String permission = strings[3];
                    if (strings[2].equalsIgnoreCase("add")) {
                        if (iPlayerInfo.getPermissionObject().getPermissions().contains(permission)) {
                            playerInfo.sendKey(playerInfo.getPrefix("RankSystem"), "core.already_has_permission", new Placeholder("%player%", iPlayerInfo.getNameWithColor()), new Placeholder("%permission%", permission));
                            return;
                        }
                        iPlayerInfo.getPermissionObject().addPermission(permission);
                        playerInfo.sendKey(playerInfo.getPrefix("RankSystem"), "core.added.permission", new Placeholder("%player%", iPlayerInfo.getNameWithColor()), new Placeholder("%permission%", permission));
                    } else if (strings[2].equalsIgnoreCase("remove")) {
                        if (!iPlayerInfo.getPermissionObject().getPermissions().contains(permission)) {
                            playerInfo.sendKey(playerInfo.getPrefix("RankSystem"), "core.does_not_has_permission", new Placeholder("%player%", iPlayerInfo.getNameWithColor()), new Placeholder("%permission%", permission));
                            return;
                        }
                        iPlayerInfo.getPermissionObject().removePermission(permission);
                        playerInfo.sendKey(playerInfo.getPrefix("RankSystem"), "core.removed.permission", new Placeholder("%player%", iPlayerInfo.getNameWithColor()), new Placeholder("%permission%", permission));
                    }
                }, () -> {
                    playerInfo.sendKey(playerInfo.getPrefix("RankSystem"), "core.rank.player_not_found", new Placeholder("%player%", strings[1]));
                });
                return false;
            } else if (strings[0].equalsIgnoreCase("children")) {
                AbstractPermissionGroup permissionGroup = PermissionGroup.getGroupByName(strings[1].replace("-", " "));
                if (permissionGroup != null) {
                    AbstractPermissionGroup childrenGroup = PermissionGroup.getGroupByName(strings[3].replace("-", " "));
                    if (childrenGroup != null) {
                        if (!playerInfo.getPermissionObject().getCurrentGroup().getGroup().getName().equals(PermissionGroup.ADMINISTRATOR.getName()) && (permissionGroup.getName().equals(PermissionGroup.ADMINISTRATOR.getName()) || permissionGroup.getPermissions().contains("*") || childrenGroup.getName().equals(PermissionGroup.ADMINISTRATOR.getName()))) {
                            playerInfo.sendKey(playerInfo.getPrefix("RankSystem"), "core.rank.not.allowed", new Placeholder("%group%", permissionGroup.getColor() + permissionGroup.getName()));
                            return false;
                        }
                        if (strings[2].equalsIgnoreCase("add")) {
                            if (permissionGroup.getStringChildren().contains(childrenGroup.getName())) {
                                playerInfo.sendKey(playerInfo.getPrefix("RankSystem"), "core.group.already_has_children", new Placeholder("%group%", permissionGroup.getColor() + permissionGroup.getName()), new Placeholder("%children%", childrenGroup.getColor() + childrenGroup.getName()));
                                return false;
                            }
                            permissionGroup.getStringChildren().add(childrenGroup.getName());
                            Verany.updatePermissionGroup(CoreExecutor.INSTANCE, permissionGroup);
                            playerInfo.sendKey(playerInfo.getPrefix("RankSystem"), "core.group.added.children", new Placeholder("%group%", permissionGroup.getColor() + permissionGroup.getName()), new Placeholder("%children%", childrenGroup.getColor() + childrenGroup.getName()));
                        } else if (strings[2].equalsIgnoreCase("remove")) {
                            if (!permissionGroup.getStringChildren().contains(childrenGroup.getName())) {
                                playerInfo.sendKey(playerInfo.getPrefix("RankSystem"), "core.group.does_not_has_children", new Placeholder("%group%", permissionGroup.getColor() + permissionGroup.getName()), new Placeholder("%children%", childrenGroup.getColor() + childrenGroup.getName()));
                                return false;
                            }
                            permissionGroup.getStringChildren().remove(childrenGroup.getName());
                            Verany.updatePermissionGroup(CoreExecutor.INSTANCE, permissionGroup);
                            playerInfo.sendKey(playerInfo.getPrefix("RankSystem"), "core.group.removed.children", new Placeholder("%group%", permissionGroup.getColor() + permissionGroup.getName()), new Placeholder("%children%", childrenGroup.getColor() + childrenGroup.getName()));
                        }
                        return false;
                    }
                }
            }
        }

        player.sendMessage(playerInfo.getKeyArray("core.rank.help", '~', new Placeholder("%prefix%", playerInfo.getPrefix("RankSystem"))));

        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command
            command, @NotNull String s, @NotNull String[] strings) {
        Player player = (Player) commandSender;
        if (!player.hasPermission("verany.command.rank"))
            return ImmutableList.of();

        List<String> arguments = Lists.newArrayList("set", "permission", "children");

        List<String> players = new ArrayList<>();
        for (IPlayerInfo playerInfo : Verany.PROFILE_OBJECT.getRegisteredPlayers())
            players.add(playerInfo.getName());

        List<String> permissionGroups = new ArrayList<>();
        for (AbstractPermissionGroup value : PermissionGroup.VALUES)
            permissionGroups.add(value.getName().replace(" ", "-"));

        List<String> times = Lists.newArrayList("h=", "d=", "lifetime", "-1");

        List<String> permissionArguments = Lists.newArrayList("add", "remove");

        if (strings.length == 1)
            return StringUtil.copyPartialMatches(strings[0], arguments, new ArrayList<>(arguments.size()));
        else if (strings.length == 2) {
            if (strings[0].equalsIgnoreCase("set"))
                return StringUtil.copyPartialMatches(strings[1], players, new ArrayList<>(players.size()));
            else if (strings[0].equalsIgnoreCase("permission") || strings[0].equalsIgnoreCase("info")) {
                List<String> newPlayers = new ArrayList<>(players);
                newPlayers.addAll(permissionGroups);
                return StringUtil.copyPartialMatches(strings[1], newPlayers, new ArrayList<>(newPlayers.size()));
            } else if (strings[0].equalsIgnoreCase("children")) {
                return StringUtil.copyPartialMatches(strings[1], permissionGroups, new ArrayList<>(permissionGroups.size()));
            }
        } else if (strings.length == 3) {
            if (strings[0].equalsIgnoreCase("set"))
                return StringUtil.copyPartialMatches(strings[2], permissionGroups, new ArrayList<>(permissionGroups.size()));
            else if (strings[0].equalsIgnoreCase("permission") || strings[0].equalsIgnoreCase("children"))
                return StringUtil.copyPartialMatches(strings[2], permissionArguments, new ArrayList<>(permissionArguments.size()));
        } else if (strings.length == 4) {
            if (strings[0].equalsIgnoreCase("set"))
                return StringUtil.copyPartialMatches(strings[3], times, new ArrayList<>(times.size()));
            else if (strings[0].equalsIgnoreCase("permission") && strings[2].equalsIgnoreCase("remove")) {
                AbstractPermissionGroup permissionGroup = PermissionGroup.getGroupByName(strings[1].replace("-", " "));
                if (permissionGroup != null) {
                    List<String> targetPermissions = new ArrayList<>(permissionGroup.getPermissions());
                    return StringUtil.copyPartialMatches(strings[3], targetPermissions, new ArrayList<>(targetPermissions.size()));
                }
                Optional<IPlayerInfo> playerInfo = Verany.PROFILE_OBJECT.getPlayer(strings[1]);
                if (playerInfo.isPresent()) {
                    IPlayerInfo targetPlayer = playerInfo.get();
                    List<String> targetPermissions = new ArrayList<>(targetPlayer.getPermissionObject().getPermissions());
                    return StringUtil.copyPartialMatches(strings[3], targetPermissions, new ArrayList<>(targetPermissions.size()));
                }
            } else if (strings[0].equalsIgnoreCase("children")) {
                AbstractPermissionGroup permissionGroup = PermissionGroup.getGroupByName(strings[1].replace("-", " "));
                if (permissionGroup != null) {
                    List<String> targetGroups = new ArrayList<>();
                    if (strings[2].equalsIgnoreCase("add")) {
                        for (AbstractPermissionGroup value : PermissionGroup.VALUES)
                            if (!value.getName().equals(permissionGroup.getName()) && !permissionGroup.getChildren().contains(value))
                                targetGroups.add(value.getName().replace(" ", "-"));
                    } else if (strings[2].equalsIgnoreCase("remove")) {
                        for (AbstractPermissionGroup child : permissionGroup.getChildren())
                            targetGroups.add(child.getName().replace(" ", "-"));
                    }
                    return StringUtil.copyPartialMatches(strings[3], targetGroups, new ArrayList<>(targetGroups.size()));
                }
            }
        }

        return ImmutableList.of();
    }
}
