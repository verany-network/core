package net.verany.executor.commands;

import net.verany.api.Verany;
import net.verany.api.placeholder.Placeholder;
import net.verany.api.player.IPlayerInfo;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CreditsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        Player player = (Player) commandSender;
        IPlayerInfo playerInfo = Verany.PROFILE_OBJECT.getPlayer(player.getUniqueId()).get();

        if (strings.length == 1) {
            if (player.hasPermission("verany.credits.other")) {
                Verany.PROFILE_OBJECT.getPlayer(strings[0]).ifPresentOrElse(iPlayerInfo -> {
                    playerInfo.sendKey(playerInfo.getPrefix("Credits"), "credits.other", new Placeholder("%credits%", iPlayerInfo.getCreditsObject().getCreditsAsDecimal()), new Placeholder("%player%", iPlayerInfo.getNameWithColor()));
                }, () -> {
                    playerInfo.sendKey(playerInfo.getPrefix("Credits"), "credits.player_not_found", new Placeholder("%player%", strings[0]));
                });
                return false;
            }
        }

        playerInfo.sendKey(playerInfo.getPrefix("Credits"), "credits.self", new Placeholder("%credits%", playerInfo.getCreditsObject().getCreditsAsDecimal()));

        return false;
    }
}
