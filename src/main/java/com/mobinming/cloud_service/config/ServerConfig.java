package com.mobinming.cloud_service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 获取当前服务的配置
 */
@Component
public class ServerConfig {

    @Value("${server.port}")
    private int serverPort;
    @Value("${upload-bg-path}")
    private String upload_bg_path;
    public String getUrl() {
        InetAddress address = null;
        try {
            address = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return "http://"+address.getHostAddress() +":"+this.serverPort;
    }

    public String getBgImgPath(Integer userId,String fileSuffix) {
        return upload_bg_path+userId+"."+fileSuffix;
    }
    public String getBgImgLinkUrl(Integer userId,String fileSuffix) {
        return getUrl()+"/img/bg/"+userId+"."+fileSuffix;
    }
}