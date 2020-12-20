package com.mobinming.cloud_service.config;

import org.crazycake.shiro.RedisManager;
import org.springframework.beans.factory.annotation.Value;
import redis.clients.jedis.Protocol;


public class MyRedisProperties {
    private static final String DEFAULT_HOST = "127.0.0.1:6379";
    @Value("${shiro-redis.redis-manager.host}")
    private String host = DEFAULT_HOST;

    // timeout for jedis try to connect to redis server, not expire time! In milliseconds
    @Value("${shiro-redis.redis-manager.timeout}")
    private int timeout = Protocol.DEFAULT_TIMEOUT;

    @Value("${shiro-redis.redis-manager.password}")
    private String password;

    public String getHost() {
        return host;
    }

    public int getTimeout() {
        return timeout;
    }

    public String getPassword() {
        return password;
    }

    public RedisManager redisManager() {
        RedisManager redisManager = new RedisManager();
        redisManager.setHost(host);
        redisManager.setPassword(password);
        redisManager.setTimeout(timeout);
        return redisManager;
    }
}
