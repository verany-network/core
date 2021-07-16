package net.verany.executor.commands;

import com.google.common.collect.ImmutableList;
import net.verany.api.Verany;
import net.verany.api.command.AbstractCommand;
import net.verany.api.command.CommandEntry;
import net.verany.api.language.AbstractLanguage;
import net.verany.api.language.LanguageWrapper;
import net.verany.api.message.KeyBuilder;
import net.verany.api.module.VeranyProject;
import net.verany.api.placeholder.Placeholder;
import net.verany.api.player.IPlayerInfo;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LanguageCommand extends AbstractCommand implements TabCompleter {

    public LanguageCommand(VeranyProject project) {
        super(project);

        CommandEntry commandEntry = new CommandEntry("language", null, this);
        commandEntry.addAlias("sprache");

        Verany.registerCommand(project, commandEntry, (playerInfo, args) -> {

            if (args.length == 1) {
                LanguageWrapper.getLanguage(args[0]).ifPresentOrElse(abstractLanguage -> {
                    if (playerInfo.getCurrentLanguage().getName().equals(abstractLanguage.getName())) {
                        KeyBuilder languageAlreadySelected = KeyBuilder.builder().key("core.language.already_selected").prefix(playerInfo.getPrefix("Language")).build();
                        playerInfo.sendKey(languageAlreadySelected);
                        return;
                    }
                    playerInfo.setCurrentLanguage(abstractLanguage);

                    KeyBuilder languageUpdated = KeyBuilder.
                            builder().
                            key("core.language.updated").
                            placeholders(
                                    new Placeholder[]{
                                            new Placeholder("%languageName%", playerInfo.getKey("core.language.name." + abstractLanguage.getName().toLowerCase()))
                                    }
                            ).
                            prefix(playerInfo.getPrefix("Language")).
                            build();

                    playerInfo.sendKey(languageUpdated);
                }, () -> sendHelp(playerInfo));
            } else {
                sendHelp(playerInfo);
            }
        });
    }

    private void sendHelp(IPlayerInfo playerInfo) {

        KeyBuilder helpKey = KeyBuilder.builder().key("core.language.help").prefix(playerInfo.getPrefix("Language")).regex('~').build();
        playerInfo.sendKey(helpKey);
    }

    @Override
    public @Nullable
    List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String string, @NotNull String[] strings) {
        if (strings.length == 1) {
            List<String> languages = Verany.LANGUAGES.stream().map(abstractLanguage -> abstractLanguage.getName().toLowerCase()).collect(Collectors.toList());
            return StringUtil.copyPartialMatches(strings[0], languages, new ArrayList<>(languages.size()));
        }

        return ImmutableList.of();
    }
}
