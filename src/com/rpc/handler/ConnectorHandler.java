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
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.lang.reflect.Method;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * Created by ruansheng on 16/7/1.
 */
public class ConnectorHandler extends SimpleChannelInboundHandler<String>{

	private HashMap<String, Api> api = null;
	
	private Logger logger = null;
	
    public ConnectorHandler() {
    	// 加载配置文件
    	String filepath="res/api.json";
    	this.api = JsonFile.toApiProto(filepath);
    	
    	// log4j
    	this.logger = Logger.getLogger(ConnectorHandler.class);
    	PropertyConfigurator.configure("res/log4j.properties");
    }

    @Override
	protected void channelRead0(ChannelHandlerContext ctx, String body) throws Exception {        
        // 将字符串用redis协议解析
        String strings[] = StringToArray.convertStrToArray(body);
        
        String uuid = Uuid.getUUID();
        
        if(strings.length == 3 && strings[2].equalsIgnoreCase("PING")) {
        	logger.info(strings[2] + " --> PONG");
        	Response response = new Response(uuid, 200, "success");
        	response.setResult(new String("PONG"));
            ctx.writeAndFlush(this.buildRespBody(response));
            return ;
        }
        
        if(strings.length != 5) {
        	logger.info(strings[4] + " --> redis protocal is error");
        	Response response = new Response(uuid, 401, "redis protocal is error");
            ctx.writeAndFlush(this.buildRespBody(response));
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
        	logger.info(action + " --> action not exists");
        	Response response = new Response(uuid, 401, "action not exists");
            ctx.writeAndFlush(this.buildRespBody(response));
            return ;
        }
    	
        // 通过反射调用方法
        Api api_obj = api.get(action);
    	String interClass = api_obj.getInterClass();
    	Class<?> class_obj = Class.forName(interClass);
    	Object api_class = class_obj.newInstance();
    	Method method = class_obj.getMethod(m, types);
    	Object ret_obj = method.invoke(api_class, objs);
    	
    	logger.info(ret_obj + " --> invoke is ok");
    	
    	// 构建Response
    	Response response = new Response(uuid, 200, "success");
    	response.setResult(ret_obj);
    	
    	// 返回结果
        ctx.writeAndFlush(this.buildRespBody(response));
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
     * @param response
     * @return
     */
    private ByteBuf buildRespBody(Response response) {
    	String cmd = JSON.toJSON(response).toString();
    	logger.info(cmd + " --> return is ok");
    	String redis_cmd = this.bulidRedisString(cmd);
    	
    	redis_cmd += System.getProperty("line.separator");
        ByteBuf resp = Unpooled.copiedBuffer(redis_cmd.getBytes());
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
