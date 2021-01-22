package com.mobinming.cloud_service.nettyWebSocket;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class Message {
    private Integer sendId;
    private Integer acceptId;
    private LocalDateTime sentTime;
    private String msg;
    private MessageType messageType;
    private boolean isRead;

    public boolean isLegal(){
        return sendId!=null&&acceptId!=null&&sentTime!=null&&msg!=null&& messageType !=null;
    }
}
