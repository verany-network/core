package net.verany.api.listener;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.verany.api.Verany;
import net.verany.api.chat.request.ChatRequest;
import net.verany.api.chat.request.ChatRequestCallback;
import net.verany.api.chat.request.NumberChatRequest;
import net.verany.api.chat.request.StringChatRequest;
import net.verany.api.config.IngameConfig;
import net.verany.api.module.VeranyPlugin;
import net.verany.api.player.IPlayerInfo;
import net.verany.api.player.permission.IPermissionObject;
import net.verany.api.player.permission.group.AbstractPermissionGroup;
import net.verany.volcano.player.IVolcanoPlayer;
import net.verany.volcano.round.GameSetting;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;

public record ChatListener(VeranyPlugin plugin) implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void handleChat(AsyncChatEvent event) {
        Player player = event.getPlayer();
        String message = Verany.serializer.serialize(event.message());

        if (player.hasMetadata("chat.request")) {

            event.setCancelled(true);

            Document document = (Document) player.getMetadata("chat.request").get(0).value();
            ChatRequest<?> request = document.get("request", ChatRequest.class);
            ChatRequestCallback callback = document.get("callback", ChatRequestCallback.class);

            if (message.equalsIgnoreCase("exit")) {
                callback.accept(ChatRequestCallback.FinishType.CANCELLED);
                plugin.removeMetadata(player, "chat.request");
                return;
            }

            ChatRequestCallback.FinishType finishType = ChatRequestCallback.FinishType.SUCCESS;
            try {
                if (request instanceof NumberChatRequest chatRequest) {
                    int i = Integer.parseInt(message);
                    if (i < chatRequest.getMin() || i > chatRequest.getMax()) {
                        finishType = ChatRequestCallback.FinishType.FAIL.message("Value not allowed!");
                        return;
                    }
                    chatRequest.setValue(i);
                    return;
                }
                if (request instanceof StringChatRequest stringRequest) {
                    if (message.split(" ").length > stringRequest.getMaxArguments()) {
                        finishType = ChatRequestCallback.FinishType.FAIL.message("Too many arguments!");
                        return;
                    }
                    stringRequest.setValue(message);
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
                finishType = ChatRequestCallback.FinishType.FAIL.message("An error occurred (" + e + ")");
            } finally {
                if (callback.getOnlyReset().contains(finishType))
                    plugin.removeMetadata(player, "chat.request");
                callback.accept(finishType);
            }
            return;
        }

        if (!IngameConfig.CHAT.getValue()) return;

        event.setCancelled(true);

        IPlayerInfo playerInfo = Verany.getPlayer(player);

        Collection<? extends Player> players = Bukkit.getOnlinePlayers();

        if (playerInfo.getPlayer(IVolcanoPlayer.class) != null)
            if (playerInfo.getPlayer(IVolcanoPlayer.class).getRound().getSettingValue(GameSetting.CHAT))
                players = playerInfo.getPlayer(IVolcanoPlayer.class).getRound().getBukkitPlayers();
            else
                players = new ArrayList<>();

        IPermissionObject permissionObject = playerInfo.getPermissionObject();
        AbstractPermissionGroup currentGroup = permissionObject.getCurrentGroup().getGroup();
        String newMessage = MessageFormat.format(IngameConfig.CHAT_FORMAT.getValue(), ChatColor.valueOf(currentGroup.getColor()), currentGroup.getName(), player.getName(), message);
        for (Player bukkitPlayer : players) {
            if (player.hasPermission("verany.chat") && IngameConfig.COLORED_CHAT.getValue())
                newMessage = ChatColor.translateAlternateColorCodes('&', newMessage);
            bukkitPlayer.sendMessage(newMessage);
        }
    }

}
