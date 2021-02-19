package net.verany.executor.commands;

import net.verany.api.Verany;
import net.verany.api.language.EnumLanguage;
import net.verany.api.placeholder.Placeholder;
import net.verany.api.player.IPlayerInfo;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class LanguageCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        Player player = (Player) commandSender;
        IPlayerInfo playerInfo = Verany.PROFILE_OBJECT.getPlayer(player.getUniqueId()).get();

        if (strings.length == 1) {
            EnumLanguage language = EnumLanguage.valueOf(strings[0].toUpperCase(Locale.ROOT));
            playerInfo.setLanguage(language);
            playerInfo.sendKey(Verany.getPrefix("CoreExecutor", playerInfo.getPrefixPattern()), "language.updated", new Placeholder("%language%", Verany.getNameOfEnum(language.name(), "").replaceFirst(" ", "")));
        }

        return false;
    }
}
