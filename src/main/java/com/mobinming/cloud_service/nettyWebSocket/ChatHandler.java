package com.mobinming.cloud_service.nettyWebSocket;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mobinming.cloud_service.config.GSONConfig;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.GlobalEventExecutor;

//TextWebSocketFrame：处理消息的handler，在Netty中用于处理文本的对象，frames是消息的载体
public class ChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    public AttributeKey<String> key = AttributeKey.valueOf("userId");
    //

    /**
     * 判断一个通道是否有用户在使用
     * 可做信息转发时判断该通道是否合法
     *
     * @param channel
     * @return
     */
    public boolean hasUser(Channel channel) {
        return (channel.hasAttr(key) || channel.attr(key).get() != null);//netty移除了这个map的remove方法,这里的判断谨慎一点
    }

    /**
     * 上线一个用户
     *
     * @param channel
     * @param userId
     */
    public void online(Channel channel, String userId) {
        //先判断用户是否在web系统中登录?
        //这部分代码个人实现,参考上面redis中的验证
        channel.attr(key).set(userId);
        WebSocketServer.channelMap.put(userId, channel);
    }

    /**
     * 根据用户id获取该用户的通道
     *
     * @param userId
     * @return
     */
    public Channel getChannelByUserId(String userId) {
        return WebSocketServer.channelMap.get(userId);
    }

    /**
     * 判断一个用户是否在线
     *
     * @param userId
     * @return
     */
    public Boolean online(String userId) {
        return WebSocketServer.channelMap.containsKey(userId) && WebSocketServer.channelMap.get(userId) != null;
    }

    public void remove(ChannelHandlerContext ctx) {
        Attribute<String> channelAttr = ctx.channel().attr(key);
        //基于channel的属性
        String phone = channelAttr.get();

        if (phone != null) {
            WebSocketServer.channelMap.remove(phone);
        }
    }

    public String getUserId(ChannelHandlerContext ctx) {
        Attribute<String> channelAttr = ctx.channel().attr(key);
        //基于channel的属性
        return channelAttr.get();
    }

    //用于记录和管理所有客户端的channel，可以把相应的channel保存到一整个组中
    //DefaultChannelGroup：用于对应ChannelGroup，进行初始化
    private static ChannelGroup channelClient = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        //text()获取从客户端发送过来的字符串


        String content = msg.text();
        Gson gson=GSONConfig.create();
        Message message = gson.fromJson(content, new TypeToken<Message>(){}.getType());
        if (message.isLegal()) {

            switch (message.getMessageType()) {
                case CHAR:

                    if (online(String.valueOf(message.getAcceptId()))) {
                        getChannelByUserId(String.valueOf(message.getAcceptId())).writeAndFlush(new TextWebSocketFrame(JSONObject.toJSONString(message)));
                        message.setMsg("发送成功");
                    } else {
                        message.setMsg(message.getAcceptId() + "不在线");

                    }
                    System.out.println(message.getMsg());
                    message.setMessageType(MessageType.SERVICE);
                    ctx.channel().writeAndFlush(new TextWebSocketFrame(JSONObject.toJSONString(message)));

                    break;
                case CHARS:

                    break;

                case LINK:
                    System.out.println("用户" + message.getSendId() + "上线");
                    online(ctx.channel(), String.valueOf(message.getSendId()));
                    message.setMessageType(MessageType.SERVICE);
                    message.setMsg("上线成功");
                    ctx.channel().writeAndFlush(new TextWebSocketFrame(gson.toJson(message)));
                    break;
                case READ:
                    if (message.isRead()){
                        message.setMessageType(MessageType.SERVICE);
                        message.setMsg(message.getSendId() + "已读");
                        System.out.println(message.getSendId() + "已读,将通知" + message.getAcceptId());
                        getChannelByUserId(String.valueOf(message.getAcceptId())).writeAndFlush(new TextWebSocketFrame(JSONObject.toJSONString(message)));
                    }else {
                        message.setMessageType(MessageType.SERVICE);
                        message.setMsg(message.getSendId() + "已读");
                        System.out.println(message.getSendId() + "已读,将通知" + message.getAcceptId());
                        getChannelByUserId(String.valueOf(message.getAcceptId())).writeAndFlush(new TextWebSocketFrame(JSONObject.toJSONString(message)));
                    }

                    break;
                default:
                    break;
            }
        } else {
            System.out.println("连接非法");
        }


        //针对channel进行发送，客户端对应的是channel
        /**
         * 方式一
         */
//        for (Channel channel : channelClient) {
//            //循环对每一个channel对应输出即可（往缓冲区中写，写完之后再刷到客户端）
//            //注：writeAndFlush不可以使用String，因为传输的载体是一个TextWebSocketFrame，需要把消息通过载体再刷到客户端
//            channel.writeAndFlush(new TextWebSocketFrame("【服务器于 " + LocalDate.now() + "接收到消息：】 ，消息内容为：" +content));
//
//        }
        /**
         * 方式二
         channelClient.writeAndFlush(new TextWebSocketFrame("【服务器于 " + LocalDate.now() + "接收到消息：】 ，消息内容为：" +content))
         */

    }

    //当客户端连接服务端（或者是打开连接之后）
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        //获取客户端所对应的channel，添加到一个管理的容器中即可
        channelClient.add(ctx.channel());
    }

    //客户端断开
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        //实际上是多余的，只要handler被移除，client会自动的把对应的channel移除掉
        System.out.println("客户端" + getUserId(ctx) + "下线");
        remove(ctx);
        channelClient.remove(ctx.channel());
        //每一而channel都会有一个长ID与短ID
        //一开始channel就有了，系统会自动分配一串很长的字符串作为唯一的ID，如果使用asLongText()获取的ID是唯一的，asShortText()会把当前ID进行精简，精简过后可能会有重复
        System.out.println("channel的长ID：" + ctx.channel().id().asLongText());
        System.out.println("channel的短ID：" + ctx.channel().id().asShortText());
    }


}
