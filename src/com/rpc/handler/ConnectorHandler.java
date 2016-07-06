package com.rpc.handler;

import com.alibaba.fastjson.JSON;
import com.rpc.entry.Api;
import com.rpc.entry.Request;
import com.rpc.entry.Response;
import com.rpc.utils.Uuid;
import com.rpc.utils.JsonFile;
import com.rpc.utils.ParseArgs;
import com.rpc.utils.StringToArray;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * Created by ruansheng on 16/7/1.
 */
public class ConnectorHandler extends ChannelHandlerAdapter{

	private HashMap<String, Api> api = null;
	
    public ConnectorHandler() {
    	// 加载配置文件
    	String filepath="res/api.json";
    	this.api = JsonFile.toApiProto(filepath);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object obj) throws Exception {
    	// 收到请求消息
        String body = (String)obj;
        
        // 将字符串用redis协议解析
        String strings[] = StringToArray.convertStrToArray(body);
        
        String uuid = Uuid.getUUID();
        if(strings.length != 5) {
        	Response response = new Response(uuid, 401, "redis protocal is error");
        	
        	String cmd = JSON.toJSON(response).toString();
        	String redis_cmd = this.bulidRedisString(cmd);
            ctx.writeAndFlush(this.buildRespBody(redis_cmd));
            return ;
        }
        
    	String params = strings[4];
        
    	// 构建Request
    	Request request = ParseArgs.toRequest(params);
    	
    	String action = request.getAction();
    	String m = request.getM();
    	Object[] objs = request.getObjects();
    	Class<?>[] types = request.getTypes();
    	
        // 接口不存在
        if(!this.api.containsKey(action)) {
        	Response response = new Response(uuid, 401, "action not exists");
        	
        	String cmd = JSON.toJSON(response).toString();
        	String redis_cmd = this.bulidRedisString(cmd);
            ctx.writeAndFlush(this.buildRespBody(redis_cmd));
            return ;
        }
    	
        Api api_obj = api.get(action);
    	String interClass = api_obj.getInterClass();
    	
    	Class<?> class_obj = Class.forName(interClass);
    	
    	Object api_class = class_obj.newInstance();
    	
    	Method method = class_obj.getMethod(m, types);
    	
    	Object ret_obj = method.invoke(api_class, objs);
    	
    	// 构建Response
    	Response response = new Response(uuid, 200, "success");
    	response.setResult(ret_obj);
    	String ret = JSON.toJSON(response).toString();
    	        
    	// 返回结果
        String redis_cmd = this.bulidRedisString(ret);
        ctx.writeAndFlush(this.buildRespBody(redis_cmd));
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

    private String bulidRedisString(String data) {
    	int len = data.length();
    	StringBuffer sb = new StringBuffer();
    	sb.append("$");
    	sb.append(String.valueOf(len));
    	sb.append("\r\n");
    	sb.append(data);
    	sb.append("\r\n");
    	String ret = sb.toString();
    	return ret;
    }
    
}
