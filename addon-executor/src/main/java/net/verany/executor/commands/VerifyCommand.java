package net.verany.executor.commands;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.hover.content.Text;
import net.verany.api.Verany;
import net.verany.api.message.AbstractComponentBuilder;
import net.verany.api.placeholder.Placeholder;
import net.verany.api.player.IPlayerInfo;
import net.verany.api.player.verifictation.IVerificationObject;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class VerifyCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        Player player = (Player) commandSender;
        IPlayerInfo playerInfo = Verany.PROFILE_OBJECT.getPlayer(player.getName()).get();
        IVerificationObject verificationObject = playerInfo.getVerificationObject();

        switch (strings.length) {
            case 1:
                switch (strings[0].toLowerCase()) {
                    case "web": {
                        IVerificationObject.VerificationType type = IVerificationObject.VerificationType.WEB;
                        if (verificationObject.isVerified(type)) {
                            playerInfo.sendKey(Verany.getPrefix("Verification", playerInfo.getPrefixPattern()), "verification.already_verified");
                            return false;
                        }
                        if (verificationObject.requestedVerification(type)) {
                            IVerificationObject.VerificationData data = verificationObject.getVerificationData(type);
                            playerInfo.sendMessage(new AbstractComponentBuilder(Verany.getPrefix("Verification", playerInfo.getPrefixPattern()) + playerInfo.getKey("verification.web.already_requested", new Placeholder("%key%", data.getKey()), new Placeholder("%time_remaining%", Verany.formatSeconds((int) ((data.getTimestamp() + TimeUnit.MINUTES.toMillis(10) - System.currentTimeMillis()) / 1000))))) {
                                @Override
                                public void onCreate() {
                                    setClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, data.getKey()));
                                    setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(new ComponentBuilder(playerInfo.getKey("verification.web.key.hover", new Placeholder("%key%", data.getKey()))).create())));
                                }
                            });
                            return false;
                        }

                        String key = Verany.generate(16);
                        IVerificationObject.VerificationData data = new IVerificationObject.VerificationData();
                        data.setKey(key);

                        verificationObject.createVerification(type, data);

                        playerInfo.sendMessage(new AbstractComponentBuilder(Verany.getPrefix("Verification", playerInfo.getPrefixPattern()) + playerInfo.getKey("verification.web.key", new Placeholder("%key%", key))) {
                            @Override
                            public void onCreate() {
                                setClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, key));
                                setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(new ComponentBuilder(playerInfo.getKey("verification.web.key.hover", new Placeholder("%key%", key))).create())));
                            }
                        });
                        break;
                    }
                    case "teamspeak":
                    case "ts": {
                        IVerificationObject.VerificationType type = IVerificationObject.VerificationType.TEAMSPEAK;
                        if (verificationObject.isVerified(type)) {
                            playerInfo.sendKey(Verany.getPrefix("Verification", playerInfo.getPrefixPattern()), "verification.already_verified");
                            return false;
                        }
                        if (!verificationObject.requestedVerification(type)) {
                            playerInfo.sendKey(Verany.getPrefix("Verification", playerInfo.getPrefixPattern()), "verification.dont_requested", new Placeholder("%name%", player.getName()));
                            return false;
                        }
                        verificationObject.confirmVerification(type);
                        break;
                    }
                    default:
                        player.sendMessage(playerInfo.getKeyArray("verify.help", '~', new Placeholder("%prefix%", Verany.getPrefix("Verification", playerInfo.getPrefixPattern()))));
                        break;
                }
                break;
            case 2: {
                switch (strings[0].toLowerCase()) {
                    case "discord":
                    case "dc": {
                        String name = strings[1];
                        IVerificationObject.VerificationType type = IVerificationObject.VerificationType.DISCORD;
                        if (verificationObject.isVerified(type)) {
                            playerInfo.sendKey(Verany.getPrefix("Verification", playerInfo.getPrefixPattern()), "verification.already_verified");
                            return false;
                        }
                        if (verificationObject.requestedVerification(type)) {
                            IVerificationObject.VerificationData data = verificationObject.getVerificationData(type);
                            playerInfo.sendKey(Verany.getPrefix("Verification", playerInfo.getPrefixPattern()), "verification.discord.already_requested", new Placeholder("%time_remaining%", Verany.formatSeconds((int) ((data.getTimestamp() + TimeUnit.MINUTES.toMillis(10) - System.currentTimeMillis()) / 1000))));
                            return false;
                        }

                        Verany.REDIS_MANAGER.sendRequest("discord-is_online<" + name, message -> {
                            if (message.equals("online")) {
                                IVerificationObject.VerificationData data = new IVerificationObject.VerificationData();
                                data.setExtra(name);

                                verificationObject.createVerification(type, data);

                                playerInfo.sendKey(Verany.getPrefix("Verification", playerInfo.getPrefixPattern()), "verification.discord.key");
                            } else {
                                playerInfo.sendKey(playerInfo.getPrefix("Verification"), "verification.discord.not_on_server", new Placeholder("%name%", name));
                            }
                        });
                        break;
                    }
                    case "teamspeak":
                    case "ts": {
                        IVerificationObject.VerificationType type = IVerificationObject.VerificationType.TEAMSPEAK;
                        if (strings[1].equalsIgnoreCase("unlink")) {
                            if (!verificationObject.isVerified(type)) {
                                playerInfo.sendKey(Verany.getPrefix("Verification", playerInfo.getPrefixPattern()), "verification.not_verified");
                                return false;
                            }
                            verificationObject.unlink(type);
                            playerInfo.sendKey(Verany.getPrefix("Verification", playerInfo.getPrefixPattern()), "verification.unlinked");
                        }
                        break;
                    }

                    default:
                        player.sendMessage(playerInfo.getKeyArray("verify.help", '~', new Placeholder("%prefix%", Verany.getPrefix("Verification", playerInfo.getPrefixPattern()))));
                        break;
                }
                break;
            }
            default:
                player.sendMessage(playerInfo.getKeyArray("verify.help", '~', new Placeholder("%prefix%", Verany.getPrefix("Verification", playerInfo.getPrefixPattern()))));
                break;
        }
        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        List<String> arguments = Lists.newArrayList("web", "discord", "dc", "teamspeak", "ts");

        if (strings.length == 1)
            return StringUtil.copyPartialMatches(strings[0], arguments, new ArrayList<>(arguments.size()));
        return ImmutableList.of();
    }
}
