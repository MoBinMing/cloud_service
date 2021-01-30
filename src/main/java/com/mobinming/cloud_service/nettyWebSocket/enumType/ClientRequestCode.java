package com.mobinming.cloud_service.nettyWebSocket.enumType;

public enum ClientRequestCode {
    LINK("link");
    private final String name;

    ClientRequestCode(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
