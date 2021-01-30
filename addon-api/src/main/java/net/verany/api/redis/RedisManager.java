package net.verany.api.redis;

import net.verany.api.redis.redispub.RedisPubSub;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisManager {

    private final JedisPool jedisPool;
    private final RedisPubSub redisPubSub;

    public RedisManager(JedisPool jedisPool, RedisPubSub redisPubSub) {
        this.jedisPool = jedisPool;
        this.redisPubSub = redisPubSub;
        this.setupSubscriber();
    }

    private void setupSubscriber() {
        new Thread(() -> {
            for (; ; ) {
                System.out.println("1");
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
        }
    }

    public void sendMessage(String channel, String message) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.publish(channel, message);
        }
    }

    public JedisPool getJedisPool() {
        return jedisPool;
    }

}
