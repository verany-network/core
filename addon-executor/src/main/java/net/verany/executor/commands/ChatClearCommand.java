package net.verany.executor.commands;

import net.verany.api.Verany;
import net.verany.api.module.VeranyModule;
import net.verany.api.placeholder.Placeholder;
import net.verany.api.player.IPlayerInfo;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ChatClearCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        Player player = (Player) commandSender;
        IPlayerInfo playerInfo = Verany.getPlayer(player);

        if (!player.hasPermission("verany.moderator")) {

            return false;
        }

        for (int i = 0; i < 200; i++) {
            Bukkit.broadcastMessage(" ");
        }

        playerInfo.sendKey(playerInfo.getPrefix("ChatClear"), "core.chat.clear");

        if (strings.length == 1 && strings[0].equalsIgnoreCase("silent"))
            return false;

        for (IPlayerInfo onlinePlayer : Verany.getOnlinePlayers()) {
            onlinePlayer.sendKey(onlinePlayer.getPrefix("ChatClear"), "core.chat.cleared", new Placeholder("%clearer%", playerInfo.getNameWithColor()));
        }

        return false;
    }
}
