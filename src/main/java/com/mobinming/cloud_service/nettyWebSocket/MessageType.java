package com.mobinming.cloud_service.nettyWebSocket;

public enum MessageType {
    LINK("link"),CHAR("char"),CHARS("chars"),READ("read"),SERVICE("service");
    private final String name;

    MessageType(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
