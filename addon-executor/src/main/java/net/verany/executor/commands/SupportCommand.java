package net.verany.executor.commands;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import de.dytanic.cloudnet.wrapper.Wrapper;
import net.verany.api.Verany;
import net.verany.api.player.IPlayerInfo;
import net.verany.api.player.verifictation.IVerificationObject;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SupportCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        Player player = (Player) commandSender;
        IPlayerInfo playerInfo = Verany.PROFILE_OBJECT.getPlayer(player.getUniqueId()).get();

        if (!player.hasPermission("verany.command.support")) {

            return false;
        }

        if (!playerInfo.getVerificationObject().isVerified(IVerificationObject.VerificationType.TEAMSPEAK)) {

            return false;
        }

        if (strings.length == 1) {
            switch (strings[0].toLowerCase()) {
                case "accept": {

                    break;
                }
                case "close": {

                    break;
                }
            }
        }

        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        List<String> arguments = Lists.newArrayList("accept");

        if (strings.length == 1)
            return StringUtil.copyPartialMatches(strings[0], arguments, new ArrayList<>(arguments.size()));
        return ImmutableList.of();
    }
}
