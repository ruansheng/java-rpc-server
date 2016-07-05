package com.rpc.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rpc.entry.Api;
import com.rpc.entry.Request;
import com.rpc.utils.JsonFile;
import com.rpc.utils.StringToArray;
import com.rpc.utils.Types;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.omg.Dynamic.Parameter;


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
        
        if(strings.length != 5) {
        	Map<String, String> map = new HashMap<String, String>();
        	map.put("ec", "401");
        	map.put("em", "redis protocal is error");
        	String cmd = JSON.toJSON(map).toString();
        	String redis_cmd = this.bulidRedisString(cmd);
            ctx.writeAndFlush(this.buildRespBody(redis_cmd));
            return ;
        }
        
    	String params = strings[4];
        
    	System.out.println("params:" + params);
        
    	Request req = JSON.parseObject(params, Request.class);
    	
    	Object[] os = req.getParams().getArgs();
    	
    	Class<?>[] types = Types.getTypes(os);
    	
    	req.getParams().setTypes(types);
    	
    	String action = req.getAction();
    	String m = req.getParams().getM();
    	Object[] objs = req.getParams().getArgs();
    	
    	System.out.println("action:" + action);
    	System.out.println("m:" + m);
    	System.out.println("objs-length:" + objs.length);
    	System.out.println("typs-length:" + types.length);
    	System.out.println("typs-type:" + types.getClass().getName());
    	
        // 接口不存在
        if(!this.api.containsKey(action)) {
        	Map<String, String> map = new HashMap<String, String>();
        	map.put("ec", "401");
        	map.put("em", "action not exists");
        	String cmd = JSON.toJSON(map).toString();
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
    	
        String ret = ret_obj.toString();
        
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
