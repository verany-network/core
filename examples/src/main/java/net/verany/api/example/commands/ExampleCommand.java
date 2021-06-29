package net.verany.api.example.commands;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.verany.api.Verany;
import net.verany.api.chat.request.ChatRequest;
import net.verany.api.chat.request.ChatRequestCallback;
import net.verany.api.chat.request.NumberChatRequest;
import net.verany.api.chat.request.StringChatRequest;
import net.verany.api.command.AbstractCommand;
import net.verany.api.command.CommandEntry;
import net.verany.api.enumhelper.EnumHelper;
import net.verany.api.enumhelper.IdentifierType;
import net.verany.api.example.player.IExamplePlayer;
import net.verany.api.module.VeranyProject;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class ExampleCommand extends AbstractCommand implements TabCompleter {

    public ExampleCommand(VeranyProject project) {
        super(project);

        Verany.registerCommand(project, new CommandEntry("test", "command.test", this), (sender, args) -> {
            if (args.length != 1) {
                sender.sendMessage("Wrong usage! /test <name/age>");
                return;
            }

            ExampleType type = EnumHelper.INSTANCE.valueOf(args[1], ExampleType.values());
            if (type == null) {
                sender.sendMessage("This type does not exist!");
                return;
            }

            IExamplePlayer examplePlayer = sender.getPlayer(IExamplePlayer.class);

            switch (type) {
                case NAME -> {
                    ChatRequest<String> nameRequest = new StringChatRequest("Please enter your name", TimeUnit.SECONDS.toMillis(30), 1);
                    sender.requestChatMessage(project, nameRequest, new ChatRequestCallback() {
                        @Override
                        public void accept(FinishType onFinish) {
                            if (onFinish.equals(FinishType.SUCCESS)) {
                                String name = nameRequest.getValue();
                                examplePlayer.setName(name);
                                sender.sendMessage("Your name is " + name);
                                return;
                            }
                        }
                    });
                }
                case AGE -> {
                    ChatRequest<Integer> ageRequest = new NumberChatRequest("Pleaser enter your age", TimeUnit.SECONDS.toMillis(30), 0, 99);
                    sender.requestChatMessage(project, ageRequest, new ChatRequestCallback() {
                        @Override
                        public void accept(FinishType onFinish) {
                            if (onFinish.equals(FinishType.SUCCESS)) {
                                int age = ageRequest.getValue();
                                examplePlayer.setAge(age);
                                sender.sendMessage("Your age is " + age);
                            }
                        }
                    });
                }
            }

        });
    }

    @Override
    public @Nullable
    List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String string, @NotNull String[] strings) {
        List<String> arguments = Arrays.stream(ExampleType.values()).map(ExampleType::getId).collect(Collectors.toList());

        if (strings.length == 1)
            return StringUtil.copyPartialMatches(strings[0], arguments, new ArrayList<>(arguments.size()));
        return ImmutableList.of();
    }

    @AllArgsConstructor
    @Getter
    public enum ExampleType implements IdentifierType<String> {
        NAME("name"),
        AGE("age");

        private final String id;
    }
}
