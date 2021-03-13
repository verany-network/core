package net.verany.api.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import net.verany.api.Verany;
import net.verany.api.command.executor.StatsCommandExecutor;
import net.verany.api.player.IPlayerInfo;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
public class StatsCommandBuilder implements CommandExecutor {

    private final StatsCommandExecutor executor;

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        Player player = (Player) commandSender;
        IPlayerInfo playerInfo = Verany.getPlayer(player);
        IPlayerInfo target = playerInfo;

        if(strings.length == 1) {
            
        }

        return false;
    }
}
