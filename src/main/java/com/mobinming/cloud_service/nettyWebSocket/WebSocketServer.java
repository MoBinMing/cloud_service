package com.mobinming.cloud_service.nettyWebSocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.AttributeKey;
import lombok.SneakyThrows;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.BindException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用于和客户端进行连接
 *
 * @author phubing
 *
 */
@Component
@Order(value = 1)
public class WebSocketServer implements CommandLineRunner {
    public static ConcurrentHashMap<String, Channel> channelMap = new ConcurrentHashMap<>();
//    public static void main(String[] args) throws InterruptedException {
//
//
//    }

    @Override
    public void run(String... args) throws Exception {
        new Thread(){
            @SneakyThrows
            public void run() {
                //定义线程组
                EventLoopGroup mainGroup =  new NioEventLoopGroup();
                EventLoopGroup subGroup =  new NioEventLoopGroup();
                try {
                    ServerBootstrap server = new ServerBootstrap();
                    server.group(mainGroup, subGroup)
                            //channel类型
                            .channel(NioServerSocketChannel.class)
                            //针对subGroup做的子处理器，childHandler针对WebSokect的初始化器
                            .childHandler(new WebSocketinitializer());

                    //绑定端口并以同步方式进行使用
                    ChannelFuture channelFuture = server.bind(1024).sync();

                    //针对channelFuture，进行相应的监听
                    channelFuture.channel().closeFuture().sync();

                } finally {
                    //针对两个group进行优雅地关闭
                    mainGroup.shutdownGracefully();
                    subGroup.shutdownGracefully();
                }

            }
        }.start();

    }


}