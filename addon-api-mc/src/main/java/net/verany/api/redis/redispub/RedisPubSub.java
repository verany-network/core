package net.verany.api.redis.redispub;

import lombok.Setter;
import net.verany.api.AbstractVerany;
import net.verany.api.module.VeranyProject;
import net.verany.api.redis.events.VeranyMessageInEvent;
import org.bukkit.Bukkit;
import redis.clients.jedis.JedisPubSub;

@Setter
public class RedisPubSub extends JedisPubSub {

    private VeranyProject project;

    public void onMessage(String channel, String message) {
        String[] data = message.split("~");

        if (!AbstractVerany.shutdown)
            Bukkit.getScheduler().scheduleSyncDelayedTask(project, () -> Bukkit.getPluginManager().callEvent(new VeranyMessageInEvent(message)));

    }

}
