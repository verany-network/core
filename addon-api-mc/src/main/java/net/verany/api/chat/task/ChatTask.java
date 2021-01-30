package net.verany.api.chat.task;

import net.verany.api.chat.request.ChatRequestCallback;
import net.verany.api.module.VeranyProject;
import net.verany.api.plugin.IVeranyPlugin;
import net.verany.api.task.AbstractTask;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ChatTask extends AbstractTask {

    private final VeranyProject plugin;

    public ChatTask(long waitTime, VeranyProject plugin) {
        super(waitTime);
        this.plugin = plugin;
    }

    @Override
    public void run() {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (onlinePlayer.hasMetadata("chat.request")) {
                Document document = (Document) onlinePlayer.getMetadata("chat.request").get(0).value();
                ChatRequestCallback callback = document.get("callback", ChatRequestCallback.class);
                long time = document.get("timestamp", Long.class);
                if (System.currentTimeMillis() > time) {
                    callback.accept(ChatRequestCallback.FinishType.TIME_OUT.message("§cYou took too long!"));
                    onlinePlayer.removeMetadata("chat.request", plugin);
                }
            }
        }
    }
}
