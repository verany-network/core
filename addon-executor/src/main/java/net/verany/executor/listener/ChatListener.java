package net.verany.executor.listener;

import net.verany.api.Verany;
import net.verany.api.chat.request.ChatRequest;
import net.verany.api.chat.request.ChatRequestCallback;
import net.verany.api.chat.request.NumberChatRequest;
import net.verany.api.chat.request.StringChatRequest;
import net.verany.api.config.IngameConfig;
import net.verany.api.event.AbstractListener;
import net.verany.api.module.VeranyProject;
import net.verany.api.player.IPlayerInfo;
import net.verany.api.player.permission.IPermissionObject;
import net.verany.api.player.permission.group.AbstractPermissionGroup;
import net.verany.volcano.GameSetting;
import net.verany.volcano.player.IVolcanoPlayer;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ChatListener extends AbstractListener {

    public ChatListener(VeranyProject project) {
        super(project);

        Verany.registerListener(project, AsyncPlayerChatEvent.class, event -> {
            Player player = event.getPlayer();
            String message = event.getMessage();

            event.setCancelled(true);

            if (player.hasMetadata("chat.request")) {

                Document document = (Document) player.getMetadata("chat.request").get(0).value();
                ChatRequest<?> request = document.get("request", ChatRequest.class);
                ChatRequestCallback callback = document.get("callback", ChatRequestCallback.class);

                if (event.getMessage().equalsIgnoreCase("exit")) {
                    callback.accept(ChatRequestCallback.FinishType.CANCELLED);
                    project.removeMetadata(player, "chat.request");
                    return;
                }

                ChatRequestCallback.FinishType finishType = ChatRequestCallback.FinishType.SUCCESS;
                try {
                    if (request instanceof NumberChatRequest) {
                        NumberChatRequest chatRequest = (NumberChatRequest) request;
                        int i = Integer.parseInt(message);
                        if (i < chatRequest.getMin() || i > chatRequest.getMax()) {
                            finishType = ChatRequestCallback.FinishType.FAIL.message("Value not allowed!");
                            return;
                        }
                        chatRequest.setValue(i);
                        return;
                    }
                    if (request instanceof StringChatRequest) {
                        StringChatRequest stringRequest = (StringChatRequest) request;
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
                        project.removeMetadata(player, "chat.request");
                    callback.accept(finishType);
                }
                return;
            }

            IPlayerInfo playerInfo = Verany.getPlayer(player);

            Collection<? extends Player> players = Bukkit.getOnlinePlayers();

            if (playerInfo.getPlayer(IVolcanoPlayer.class) != null)
                if (playerInfo.getPlayer(IVolcanoPlayer.class).getRound().getSettingValue(GameSetting.CHAT))
                    players = playerInfo.getPlayer(IVolcanoPlayer.class).getRound().getBukkitPlayers();
                else
                    players = new ArrayList<>();

            IPermissionObject permissionObject = playerInfo.getPermissionObject();
            AbstractPermissionGroup currentGroup = permissionObject.getCurrentGroup().getGroup();
            String newMessage = Verany.format(IngameConfig.CHAT_FORMAT.getValue(), currentGroup.getColor(), currentGroup.getName(), player.getName(), message);
            for (Player bukkitPlayer : players)
                bukkitPlayer.sendMessage(newMessage);
        }, EventPriority.LOWEST);
    }
}
