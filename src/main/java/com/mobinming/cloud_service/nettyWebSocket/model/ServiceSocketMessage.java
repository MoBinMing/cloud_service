package com.mobinming.cloud_service.nettyWebSocket.model;

import com.mobinming.cloud_service.nettyWebSocket.enumType.SendType;
import lombok.Data;

import java.util.Date;

@Data
public class ServiceSocketMessage {
    private Integer code;
    private Date sentTime;
    private SendType sendType;
    private String msg;
}
