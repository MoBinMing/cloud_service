package com.mobinming.cloud_service.nettyWebSocket.clientModel;

import cn.hutool.json.JSONUtil;
import com.mobinming.cloud_service.nettyWebSocket.enumType.SendType;
import lombok.Data;

@Data
public class SocketMessage {
    private SendType sendType;
    private String sendTypeJson;

    public boolean isLegal() {
        return sendType !=null&& JSONUtil.isJson(sendTypeJson);
    }
}
