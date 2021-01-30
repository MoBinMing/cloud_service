package com.mobinming.cloud_service.nettyWebSocket.model;

import com.mobinming.cloud_service.nettyWebSocket.enumType.ClientRequestCode;
import com.mobinming.cloud_service.nettyWebSocket.enumType.SendType;
import lombok.Data;

import java.util.Date;

@Data
public class ClientSocketMessage{
    private Integer clientUserId;
    private ClientRequestCode clientRequestCode;
    private String msg;
    private Date sentTime;
    private SendType sendType;
    private String serialNumber;
}
