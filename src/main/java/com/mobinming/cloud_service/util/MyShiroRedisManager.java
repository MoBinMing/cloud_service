package com.mobinming.cloud_service.util;

import com.mobinming.cloud_service.config.MyRedisProperties;
import org.crazycake.shiro.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Protocol;

import javax.annotation.Resource;

public class MyShiroRedisManager{

    private RedisManager redisManager;

    private int database = Protocol.DEFAULT_DATABASE;

    private JedisPool jedisPool;

    public MyShiroRedisManager(RedisManager redisManager) {
        this.redisManager=redisManager;
    }

    public void init() {
        synchronized (this) {
            if (jedisPool == null) {
                String[] hostAndPort = redisManager.getHost().split(":");
                jedisPool = new JedisPool(redisManager.getJedisPoolConfig(), hostAndPort[0],
                        Integer.parseInt(hostAndPort[1]), redisManager.getTimeout(), redisManager.getPassword(), database);
            }
        }
    }

    public Jedis getJedis() {
        if (jedisPool == null) {
            init();
        }
        return jedisPool.getResource();
    }


    public int getDatabase() {
        return database;
    }

    public void setDatabase(int database) {
        this.database = database;
    }

    public JedisPool getJedisPool() {
        return jedisPool;
    }

    public void setJedisPool(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }
}
