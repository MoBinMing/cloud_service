package com.mobinming.cloud_service.nettyWebSocket.model;

import com.mobinming.cloud_service.nettyWebSocket.enumType.SendType;
import lombok.Data;
import java.util.Date;

@Data
public class CharsSocketMessage{
    private Integer sendUserId;
    private Integer toCharsId;
    private String msg;
    private Date sentTime;
    private SendType sendType;
}
