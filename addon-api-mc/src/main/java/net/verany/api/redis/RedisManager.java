package net.verany.api.redis;

import com.mongodb.util.JSON;
import lombok.SneakyThrows;
import net.verany.api.AbstractVerany;
import net.verany.api.module.VeranyProject;
import net.verany.api.redis.events.VeranyMessageOutEvent;
import net.verany.api.redis.redispub.RedisPubSub;
import org.bukkit.Bukkit;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.Serializable;

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

    @SneakyThrows
    public <T extends Serializable> T putObject(String key, T value, int expireTimeSecs) {
        if (expireTimeSecs < 0)
            throw new IllegalArgumentException(String.format("Illegal expireTimeSecs = %s", expireTimeSecs));
        try (Jedis jedis = jedisPool.getResource()) {
            String code;
            if (expireTimeSecs == 0)
                code = jedis.set(key, AbstractVerany.GSON.toJson(value));
            else
                code = jedis.setex(key, expireTimeSecs, AbstractVerany.GSON.toJson(value));
            if (!code.equalsIgnoreCase("OK"))
                throw new CacheException("");
        }
        return value;
    }

    public <T extends Serializable> T putObject(String key, T value) {
        return putObject(key, value, 0);
    }

    public <T extends Serializable> T getObject(String key, Class<T> tClass) {
        try (Jedis jedis = jedisPool.getResource()) {
            return AbstractVerany.GSON.fromJson(jedis.get(key), tClass);
        }
    }

    public Object getObject(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return JSON.parse(jedis.get(key));
        }
    }

    public JedisPool getJedisPool() {
        return jedisPool;
    }

}
