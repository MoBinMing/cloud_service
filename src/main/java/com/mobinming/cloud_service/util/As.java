package com.mobinming.cloud_service.util;

import org.crazycake.shiro.IRedisManager;

import java.util.Set;

public class As implements IRedisManager {
    @Override
    public byte[] get(byte[] key) {
        return new byte[0];
    }

    @Override
    public byte[] set(byte[] key, byte[] value, int expire) {
        return new byte[0];
    }

    @Override
    public void del(byte[] key) {

    }

    @Override
    public Long dbSize(byte[] pattern) {
        return null;
    }

    @Override
    public Set<byte[]> keys(byte[] pattern) {
        return null;
    }
}
