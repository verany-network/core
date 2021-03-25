package net.verany.api.command.executor;

import net.verany.api.player.IPlayerInfo;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public interface VeranyCommandExecutor {

    void onExecute(IPlayerInfo sender, String[] args);

    /*void onExecute(Player sender, String[] args);

    void onExecute(CommandSender sender, String[] args);*/

}
