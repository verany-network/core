package net.verany.api.redis;

import net.verany.api.module.VeranyProject;
import net.verany.api.redis.events.VeranyMessageOutEvent;
import net.verany.api.redis.redispub.RedisPubSub;
import org.bukkit.Bukkit;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisManager {

    private final JedisPool jedisPool;
    private final RedisPubSub redisPubSub;
    private VeranyProject project;

    public RedisManager(JedisPool jedisPool, RedisPubSub redisPubSub) {
        this.jedisPool = jedisPool;
        this.redisPubSub = redisPubSub;
        this.setupSubscriber();
    }

    public void load(VeranyProject project) {
        this.project = project;
        redisPubSub.setProject(project);
    }

    private void setupSubscriber() {
        new Thread(() -> {
            for (; ; ) {
                try (Jedis jedis = jedisPool.getResource()) {
                    jedis.subscribe(redisPubSub, "network");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    redisPubSub.unsubscribe();
                }
            }
        }).start();
    }

    public void sendMessage(String message) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.publish("network", message);
            Bukkit.getScheduler().scheduleSyncDelayedTask(project, () -> Bukkit.getPluginManager().callEvent(new VeranyMessageOutEvent(message)));
        }
    }

    public void sendMessage(String channel, String message) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.publish(channel, message);
            Bukkit.getScheduler().scheduleSyncDelayedTask(project, () -> Bukkit.getPluginManager().callEvent(new VeranyMessageOutEvent(message)));
        }
    }

    public JedisPool getJedisPool() {
        return jedisPool;
    }

}
