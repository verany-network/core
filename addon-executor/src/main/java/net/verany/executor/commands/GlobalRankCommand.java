package net.verany.executor.commands;

import com.google.common.collect.ImmutableList;
import com.mongodb.annotations.Immutable;
import net.verany.api.Verany;
import net.verany.api.module.VeranyModule;
import net.verany.api.placeholder.Placeholder;
import net.verany.api.player.IPlayerInfo;
import net.verany.executor.CoreExecutor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class GlobalRankCommand implements CommandExecutor, TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        Player player = (Player) commandSender;
        IPlayerInfo playerInfo = Verany.getPlayer(player);

        if (strings.length == 1) {
            if (player.hasPermission("verany.vip")) {
                Verany.PROFILE_OBJECT.getPlayer(strings[0]).ifPresentOrElse(iPlayerInfo -> {
                    playerInfo.sendKey(playerInfo.getPrefix("GlobalRank"), "core.global_rank.other", new Placeholder("%name%", iPlayerInfo.getNameWithColor()), new Placeholder("%rank%", iPlayerInfo.getGlobalRank()));
                }, () -> {
                    playerInfo.sendKey(playerInfo.getPrefix("GlobalRank"), "core.rank.player_not_found", new Placeholder("%player%", strings[0]));
                });
                return false;
            }
        }
        playerInfo.sendKey(playerInfo.getPrefix("GlobalRank"), "core.global_rank.self", new Placeholder("%rank%", playerInfo.getGlobalRank()));

        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        List<String> players = new ArrayList<>();
        for (IPlayerInfo playerInfo : Verany.PROFILE_OBJECT.getRegisteredPlayers())
            players.add(playerInfo.getName());

        if (strings.length == 1)
            if (commandSender.hasPermission("verany.vip"))
                return StringUtil.copyPartialMatches(strings[0], players, new ArrayList<>(players.size()));

        return ImmutableList.of();
    }
}
