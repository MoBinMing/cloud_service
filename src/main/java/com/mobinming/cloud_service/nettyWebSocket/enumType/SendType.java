package com.mobinming.cloud_service.nettyWebSocket.enumType;

public enum SendType {
    CHITCHAT("chitchat"),GROUP_CHAT("group_chat"),CLIENT("client"),SERVICE("service");
    private final String name;

    SendType(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
