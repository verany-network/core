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
import java.util.concurrent.TimeUnit;

public class PlayTimeCommand implements CommandExecutor {

    private final String[] colonText = {""};

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        Player player = (Player) commandSender;
        IPlayerInfo playerInfo = Verany.PROFILE_OBJECT.getPlayer(player.getUniqueId()).get();

        if (strings.length == 1) {
            if (strings[0].equalsIgnoreCase("top")) {
                playerInfo.sendKey(Verany.getPrefix("PlayTime", playerInfo.getPrefixPattern()), "playtime.top");
                int count = 1;
                for (Document document : Verany.getDocuments(CoreExecutor.INSTANCE, "players", "network", 10, "playTime")) {
                    IPlayerInfo target = Verany.PROFILE_OBJECT.getPlayer(UUID.fromString(document.getString("uuid"))).get();

                    int colon = 0;
                    String time = Verany.formatSeconds((int) TimeUnit.MILLISECONDS.toSeconds(target.getPlayTime()));

                    playerInfo.sendKey("playtime.top.time", new Placeholder("%count%", count), new Placeholder("%time%", time), new Placeholder("%player%", target.getNameWithColor()));
                    count++;
                }
            }
        } else {
            playerInfo.sendKey(Verany.getPrefix("PlayTime", playerInfo.getPrefixPattern()), "playtime.top.self", new Placeholder("%ranking%", Verany.getRanking(CoreExecutor.INSTANCE, player.getUniqueId(), "players", "network", "playTime")), new Placeholder("%time%", Verany.formatSeconds((int) (playerInfo.getPlayTime() / 1000))));
        }
        return false;
    }
}
