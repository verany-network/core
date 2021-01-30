package net.verany.executor.commands;

import net.verany.api.Verany;
import net.verany.api.placeholder.Placeholder;
import net.verany.api.player.IPlayerInfo;
import net.verany.executor.CoreExecutor;
import org.bson.Document;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class OnlineTimeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        Player player = (Player) commandSender;
        IPlayerInfo playerInfo = Verany.PROFILE_OBJECT.getPlayer(player.getUniqueId()).get();

        if (strings.length == 1) {
            if (strings[0].equalsIgnoreCase("top")) {
                playerInfo.sendKey(Verany.getPrefix("OnlineTime", playerInfo.getPrefixPattern()), "onlinetime.top");
                int count = 1;
                for (Document document : Verany.getDocuments(CoreExecutor.INSTANCE, "players", 10, "onlineTime")) {
                    IPlayerInfo target = Verany.PROFILE_OBJECT.getPlayer(UUID.fromString(document.getString("uuid"))).get();
                    playerInfo.sendKey("onlinetime.top.time", new Placeholder("%count%", count), new Placeholder("%time%", Verany.formatSeconds((int) (target.getOnlineTime() / 1000))), new Placeholder("%player%", target.getNameWithColor()));
                    count++;
                }
            }
        } else {
            playerInfo.sendKey(Verany.getPrefix("PlayTime", playerInfo.getPrefixPattern()), "onlinetime.top.self", new Placeholder("%ranking%", Verany.getRanking(CoreExecutor.INSTANCE, player.getUniqueId(), "players", "network", "onlineTime")), new Placeholder("%time%", Verany.formatSeconds((int) (playerInfo.getOnlineTime() / 1000))));
        }

        return false;
    }
}
