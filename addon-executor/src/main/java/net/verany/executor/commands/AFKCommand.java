package net.verany.executor.commands;

import net.verany.api.Verany;
import net.verany.api.config.IngameConfig;
import net.verany.api.player.IPlayerInfo;
import net.verany.api.player.afk.AfkObject;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class AFKCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        Player player = (Player) commandSender;
        IPlayerInfo playerInfo = Verany.PROFILE_OBJECT.getPlayer(player.getName()).get();

        if (!IngameConfig.AFK.getValue()) {
            playerInfo.sendKey(Verany.getPrefix("AFK", playerInfo.getPrefixPattern()), "afk.disabled");
            return false;
        }

        if (playerInfo.getAfkObject().isAfk()) {
            playerInfo.getAfkObject().resetAfkTime();
        } else {
            ((AfkObject) playerInfo.getAfkObject()).setAfkTime(System.currentTimeMillis() - IngameConfig.AFK_TIME.getValue());
        }

        return false;
    }
}
