package net.verany.api.redis.redispub;

import lombok.Setter;
import net.verany.api.module.VeranyProject;
import org.bukkit.Bukkit;
import redis.clients.jedis.JedisPubSub;

import java.util.ArrayList;
import java.util.List;

@Setter
public class RedisPubSub extends JedisPubSub {

    private VeranyProject project;

    public static List<RedisPubSub> pubSubs = new ArrayList<>();

    public void onMessage(String channel, String message) {
        String[] data = message.split("~");
        String[] packetData = message.split("´");
        pubSubs.forEach(redisPubSub -> redisPubSub.messageIn(channel, message));
    }

    public void messageIn(String channel, String message) {
    }

}
