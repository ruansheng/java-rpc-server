package com.rpc.handler;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.group.*;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ruansheng on 16/7/1.
 */
public class ConnectorHandler extends ChannelHandlerAdapter{

    public ConnectorHandler() {
        System.out.println("Connectorhandler");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object obj) throws Exception {
        // 收到请求消息
        String body = (String)obj;
        
        // 执行
        
        // 返回结果
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
    }

    /**
     * 构建response data
     * @param body
     * @return
     */
    private ByteBuf buildRespBody(String body) {
        body += System.getProperty("line.separator");
        ByteBuf resp = Unpooled.copiedBuffer(body.getBytes());
        return resp;
    }

}
