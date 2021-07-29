package net.verany.api.chat.task;

import net.verany.api.module.VeranyProject;

public record ChatTask(VeranyProject plugin) implements Runnable {

    @Override
    public void run() {
        /*for (IVeranyPlayer onlinePlayer : AbstractVerany.PROFILE_OBJECT.getRegisteredPlayers()) {
            if (onlinePlayer.hasMetadata("chat.request")) {
                Document document = (Document) onlinePlayer.getMetadata("chat.request").get(0).value();
                ChatRequestCallback callback = document.get("callback", ChatRequestCallback.class);
                long time = document.get("timestamp", Long.class);
                if (System.currentTimeMillis() > time) {
                    callback.accept(ChatRequestCallback.FinishType.TIME_OUT.message("§cYou took too long!"));
                    plugin.removeMetadata(onlinePlayer, "chat.request");
                }
            }
        }*/
    }
}
