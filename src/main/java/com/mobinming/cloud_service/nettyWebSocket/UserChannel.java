package com.mobinming.cloud_service.nettyWebSocket;

import io.netty.channel.Channel;
import lombok.Data;

import java.util.concurrent.ConcurrentHashMap;

@Data
public class UserChannel {
    private Integer userId;
    private ConcurrentHashMap<String, Channel> map;
    private String sn;
    private Channel channel;

    public Channel getChannel() {
        return channel;
    }
}
