package com.mobinming.cloud_service.nettyWebSocket;

import com.alibaba.fastjson.JSON;
import com.google.gson.reflect.TypeToken;
import com.mobinming.cloud_service.entity.ChitchatMessage;
import com.mobinming.cloud_service.nettyWebSocket.clientModel.SocketMessage;
import com.mobinming.cloud_service.nettyWebSocket.enumType.ClientRequestCode;
import com.mobinming.cloud_service.nettyWebSocket.model.CharsSocketMessage;
import com.mobinming.cloud_service.nettyWebSocket.model.ClientSocketMessage;
import com.mobinming.cloud_service.nettyWebSocket.model.ServiceSocketMessage;
import com.mobinming.cloud_service.nettyWebSocket.enumType.SendType;
import com.mobinming.cloud_service.service.ChitchatMessageService;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
//TextWebSocketFrame：处理消息的handler，在Netty中用于处理文本的对象，frames是消息的载体
public class ChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    @Resource
    private ChitchatMessageService chitchatMessageService;

    public static AttributeKey<String> key = AttributeKey.valueOf("userId");
    public static AttributeKey<String> serialNumberKey = AttributeKey.valueOf("SerialNumber");

    /**
     * 判断一个通道是否有用户在使用
     * 可做信息转发时判断该通道是否合法
     *
     * @param channel 该用户的通道
     * @return true在使用
     */
    public static boolean hasUser(Channel channel) {
        return (channel.hasAttr(key) || channel.attr(key).get() != null);//netty移除了这个map的remove方法,这里的判断谨慎一点
    }

    /**
     * 上线一个用户
     *
     * @param channel 该用户的通道
     * @param userId  用户id
     */
    public static void online(Channel channel, String userId, String serialNumber) {
        //先判断用户是否在web系统中登录?
        //这部分代码个人实现,参考上面redis中的验证
        channel.attr(key).set(userId);
        channel.attr(serialNumberKey).set(serialNumber);
        if (WebSocketServer.channelMap.get(userId) == null) {
            ConcurrentHashMap<String, Channel> snMap = new ConcurrentHashMap<>();
            snMap.put(serialNumber, channel);
            WebSocketServer.channelMap.put(userId, snMap);
        } else {
            WebSocketServer.channelMap.get(userId).put(serialNumber, channel);
        }
        log.info("用户【 "+userId+" 】设备：" + serialNumber + "、" + new Date() + "上线");
        log.info("用户【 "+userId+" 】、在线设备数："+WebSocketServer.channelMap.get(userId).size()+"个");
        for (String k:getChannelByUserId(userId).keySet()) {
            log.info( "设备："+k+"");
        }
    }

    /**
     * 根据用户id获取该用户的所有通道
     *
     * @param userId 用户id
     * @return Channel该用户的通道
     */
    public static ConcurrentHashMap<String, Channel> getChannelByUserId(String userId) {
        return WebSocketServer.channelMap.get(userId);
    }

    /**
     * 根据用户id获取该用户的所有通道
     *
     * @param userId 用户id
     * @return Channel该用户的通道
     */
    public static Channel getChannelByUserId(String userId, String serialNumber) {
        return getChannelByUserId(userId).get(serialNumber);
    }

    /**
     * 判断一个用户是否在线
     *
     * @param userId 用户Id
     * @return true在线、false不在线
     */
    public static Boolean isOnline(String userId) {
        return WebSocketServer.channelMap.containsKey(userId) && WebSocketServer.channelMap.get(userId) != null;
    }

    /**
     * 下线一个用户
     *
     * @param ctx ChannelHandlerContext
     */
    public static void remove(ChannelHandlerContext ctx) {
        Attribute<String> channelAttr = ctx.channel().attr(key);
        //基于channel的属性
        String userId = channelAttr.get();

        String sn = ctx.channel().attr(serialNumberKey).get();
        if (userId != null && sn != null) {
            WebSocketServer.channelMap.get(userId).remove(sn);
        }
        log.info("用户【 "+userId+" 】设备：" + sn + "、" + new Date() + "下线");
        channelClient.remove(getChannelByUserId(userId, sn));
        log.info("用户【 "+userId+" 】、在线设备数："+WebSocketServer.channelMap.get(userId).size()+"个");
        for (String k:getChannelByUserId(userId).keySet()) {
            log.info( "设备："+k+"");
        }
    }

    /**
     * 下线一个用户
     *
     * @param userId 用户id
     */
    public static void remove(String userId, String serialNumber) {
        if (userId != null && serialNumber != null) {
            WebSocketServer.channelMap.get(userId).remove(serialNumber);
        }
        Channel channel = getChannelByUserId(userId, serialNumber);
        if (channel != null) {
            channelClient.remove(channel);
        }
    }

    /**
     * 获取用户Id
     *
     * @param ctx ChannelHandlerContext
     */
    public static Integer getUserId(ChannelHandlerContext ctx) {
        Attribute<String> channelAttr = ctx.channel().attr(key);
        String userId=channelAttr.get();
        //基于channel的属性
        return Integer.valueOf(userId);
    }

    /**
     * 用于记录和管理所有客户端的channel，可以把相应的channel保存到一整个组中
     * DefaultChannelGroup：用于对应ChannelGroup，进行初始化
     */
    private static ChannelGroup channelClient = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        //text()获取从客户端发送过来的字符串
        String content = msg.text();
        SocketMessage socketMessage = JSON.parseObject(content, new TypeToken<SocketMessage>() {
        }.getType());
        if (socketMessage.isLegal()) {
            switch (socketMessage.getSendType()) {
                case CHITCHAT:
                    ChitchatMessage chitchatMessage = JSON.parseObject(socketMessage.getSendTypeJson(),
                            new TypeToken<ChitchatMessage>() {
                            }.getType());
                    if (chitchatMessage != null && chitchatMessage.isNoEmpty()) {
                        chitchatMessage.setSentTime(new Date());
                        chitchatMessage.setIsSend(0);
                        if (isOnline(String.valueOf(chitchatMessage.getToUserId()))) {
                            chitchatMessage.setIsSend(1);
                        }
                        if (chitchatMessageService.save(chitchatMessage)) {
                            sendMessage(chitchatMessage);
                            sendServiceSocketMessage(chitchatMessage.getSendUserId(), 200, "发送成功");
                        } else {
                            sendServiceSocketMessage(chitchatMessage.getSendUserId(), 400, "发送失败");
                        }
                    }
                    break;
                case GROUP_CHAT:

                    break;

                case CLIENT:
                    ClientSocketMessage clientSocketMessage = JSON.parseObject(socketMessage.getSendTypeJson(),
                            new TypeToken<ClientSocketMessage>() {
                            }.getType());
                    String userId = String.valueOf(clientSocketMessage.getClientUserId());
                    if (clientSocketMessage.getClientRequestCode() == ClientRequestCode.LINK) {
                        online(ctx.channel(), userId, clientSocketMessage.getSerialNumber());
                        sendServiceSocketMessage(userId,clientSocketMessage.getSerialNumber(), 200, "上线成功");
                        sendServiceSocketMessageNotSn(userId,clientSocketMessage.getSerialNumber(),
                                200, "当前多设备登录"+clientSocketMessage.getSerialNumber());
                    }
                    break;
                default:
                    break;
            }
        } else {
            log.info("连接非法");
            sendMessage(ctx,"连接非法");
        }


        //针对channel进行发送，客户端对应的是channel
        /**
         * 方式一
         */
//        for (Channel channel : channelClient) {
//            //循环对每一个channel对应输出即可（往缓冲区中写，写完之后再刷到客户端）
//            //注：writeAndFlush不可以使用String，因为传输的载体是一个TextWebSocketFrame，需要把消息通过载体再刷到客户端
//            channel.writeAndFlush(new TextWebSocketFrame("【服务器于 " + Localnew Date() + "接收到消息：】 ，消息内容为：" +content));
//
//        }
        /**
         * 方式二
         channelClient.writeAndFlush(new TextWebSocketFrame("【服务器于 " + Localnew Date() + "接收到消息：】 ，消息内容为：" +content))
         */

    }

    /**
     * 当客户端连接服务端（或者是打开连接之后）
     *
     * @param ctx ChannelHandlerContext通道
     * @throws Exception 报错
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        //获取客户端所对应的channel，添加到一个管理的容器中即可
        channelClient.add(ctx.channel());
    }

    /**
     * 当客户端连客户端断开服务端
     *
     * @param ctx ChannelHandlerContext通道
     * @throws Exception 报错
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        //实际上是多余的，只要handler被移除，client会自动的把对应的channel移除掉
        remove(ctx);
        //每一而channel都会有一个长ID与短ID
        //一开始channel就有了，系统会自动分配一串很长的字符串作为唯一的ID，如果使用asLongText()获取的ID是唯一的，asShortText()会把当前ID进行精简，精简过后可能会有重复
        //System.out.println("channel的长ID：" + ctx.channel().id().asLongText());
        //System.out.println("channel的短ID：" + ctx.channel().id().asShortText());
    }

    public static void sendMessage(ChitchatMessage chitchatMessage) {
        ConcurrentHashMap<String, Channel> channels = getChannelByUserId(String.valueOf(chitchatMessage.getToUserId()));
        for (Channel c : channels.values()) {
            c.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(chitchatMessage)));
        }
    }

    public static void sendMessage(CharsSocketMessage charsSocketMessage) {
        //getChannelByUserId(String.valueOf(charsSocketMessage.getToCharsId())).writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(charsSocketMessage)));
    }

    public static void sendServiceSocketMessage(Integer userId, Integer code, String msg) {
        ServiceSocketMessage serviceSocketMessage = new ServiceSocketMessage();
        serviceSocketMessage.setCode(code);
        serviceSocketMessage.setMsg(msg);
        serviceSocketMessage.setSentTime(new Date());
        serviceSocketMessage.setSendType(SendType.SERVICE);
        ConcurrentHashMap<String, Channel> channels = getChannelByUserId(String.valueOf(userId));
        for (Channel c : channels.values()) {
            c.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(serviceSocketMessage)));
        }
    }

    public static void sendServiceSocketMessage(String userId, Integer code, String msg) {
        ServiceSocketMessage serviceSocketMessage = new ServiceSocketMessage();
        serviceSocketMessage.setCode(code);
        serviceSocketMessage.setMsg(msg);
        serviceSocketMessage.setSentTime(new Date());
        serviceSocketMessage.setSendType(SendType.SERVICE);
        ConcurrentHashMap<String, Channel> channels = getChannelByUserId(userId);
        for (Channel c : channels.values()) {
            c.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(serviceSocketMessage)));
        }
    }

    public static void sendServiceSocketMessage(String userId,String sn, Integer code, String msg) {
        ServiceSocketMessage serviceSocketMessage = new ServiceSocketMessage();
        serviceSocketMessage.setCode(code);
        serviceSocketMessage.setMsg(msg);
        serviceSocketMessage.setSentTime(new Date());
        serviceSocketMessage.setSendType(SendType.SERVICE);
        ConcurrentHashMap<String, Channel> channels = getChannelByUserId(userId);
        for (Channel c : channels.values()) {
            if (c.attr(serialNumberKey).get().equals(sn)){
                c.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(serviceSocketMessage)));
            }
        }
    }

    public static void sendServiceSocketMessageNotSn(String userId,String sn, Integer code, String msg) {
        ServiceSocketMessage serviceSocketMessage = new ServiceSocketMessage();
        serviceSocketMessage.setCode(code);
        serviceSocketMessage.setMsg(msg);
        serviceSocketMessage.setSentTime(new Date());
        serviceSocketMessage.setSendType(SendType.SERVICE);
        ConcurrentHashMap<String, Channel> channels = getChannelByUserId(userId);
        for (Channel c : channels.values()) {
            if (!c.attr(serialNumberKey).get().equals(sn)){
                c.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(serviceSocketMessage)));
            }
        }
    }

    public static void sendMessage(String userId, SocketMessage socketMessage) {
        ConcurrentHashMap<String, Channel> channels = getChannelByUserId(String.valueOf(userId));
        for (Channel c : channels.values()) {
            c.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(socketMessage)));
        }
    }

    public static void sendMessage(ChannelHandlerContext ctx, SocketMessage socketMessage) {
        ctx.channel().writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(socketMessage)));
    }

    public static void sendMessage(ChannelHandlerContext ctx, String msg) {
        ctx.channel().writeAndFlush(new TextWebSocketFrame(msg));
    }
}
