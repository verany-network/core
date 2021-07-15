package net.verany.executor.commands;

import com.google.common.collect.ImmutableList;
import com.google.common.util.concurrent.ListenableScheduledFuture;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Content;
import net.md_5.bungee.api.chat.hover.content.Text;
import net.verany.api.Verany;
import net.verany.api.command.AbstractCommand;
import net.verany.api.command.CommandEntry;
import net.verany.api.message.AbstractComponentBuilder;
import net.verany.api.message.MessageData;
import net.verany.api.metadata.IMetadataInfo;
import net.verany.api.module.VeranyProject;
import net.verany.api.player.IPlayerInfo;
import net.verany.executor.CoreExecutor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class RemoveDuplicateKeyCommand extends AbstractCommand implements TabCompleter {

    public RemoveDuplicateKeyCommand(VeranyProject project) {
        super(project);

        CommandEntry commandEntry = new CommandEntry("removeduplicatekeys", "verany.command.rmdup", this);
        commandEntry.addAlias("rmdk");

        Verany.registerCommand(project, commandEntry, (playerInfo, args) -> {
            Player player = playerInfo.getPlayer();

            int currentPage = playerInfo.getPage("remover");
            int limit = 10;

            if (args.length == 0) {
                DuplicateRemover remover = new DuplicateRemover(playerInfo);
                remover.check(() -> {
                    List<String> duplicates = remover.getDuplicates();
                    int maxPages = Verany.getMaxPages(duplicates.size(), limit);
                    List<String> duplicateList = Verany.getPageList(currentPage, limit, duplicates);
                    player.sendMessage(Verany.format("Duplicates found ({0} / {1}):", duplicateList.size(), duplicates.size()));
                    for (String duplicate : duplicateList) {
                        playerInfo.sendMessage(new AbstractComponentBuilder(" - " + duplicate + " ") {
                            @Override
                            public void onCreate() {
                                TextComponent clicker = new TextComponent("(REMOVE)");
                                clicker.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/rmdk remove " + duplicate));
                                clicker.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Click, to remove the key " + duplicate + " of the remove list.")));
                                addExtra(clicker);
                            }
                        });
                    }
                    player.sendMessage(Verany.format("Page ({0} / {1})", currentPage, maxPages));
                    player.sendMessage(" ");
                });
                CoreExecutor.INSTANCE.setMetadata(player, DuplicateRemover.class, remover);
                return;
            }

            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("accept")) {
                    DuplicateRemover remover = CoreExecutor.INSTANCE.getMetadata(player, DuplicateRemover.class);
                    return;
                }
            }
            if (args.length == 2) {
                if (args[0].equalsIgnoreCase("remove")) {
                    String name = args[1];
                    DuplicateRemover remover = CoreExecutor.INSTANCE.getMetadata(player, DuplicateRemover.class);
                    remover.getDuplicates().remove(name);
                    return;
                }
            }
        });
    }

    @Override
    public @Nullable
    List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String string, @NotNull String[] strings) {
        return ImmutableList.of();
    }


    @RequiredArgsConstructor
    @Getter
    public static class DuplicateRemover implements IMetadataInfo {

        private final IPlayerInfo playerInfo;
        private final List<String> checked = new ArrayList<>();
        private final List<String> duplicates = new ArrayList<>();

        public void check(Runnable onFinish) {
            for (MessageData messageData : Verany.MESSAGES) {
                if (!messageData.isEdited() && checked.contains(messageData.getKey()))
                    duplicates.add(messageData.getKey());
                checked.add(messageData.getKey());
            }
            onFinish.run();
        }

        public void removeDuplicates() {

        }
    }
}
