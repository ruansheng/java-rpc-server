package com.rpc.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rpc.entry.Api;
import com.rpc.entry.Cmd;
import com.rpc.utils.JsonFile;
import com.rpc.utils.StringToArray;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;


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
        if(strings.length == 5) {
        	String params = strings[4];
        	
        	Cmd objs = JSON.parseObject(params, Cmd.class);
        	
            String action = objs.getAction();
            String method = objs.getParams().getM();
            String args = objs.getParams().getArgs();
            
            // 接口不存在
            if(!this.api.containsKey(action)) {
            	Map<String, String> map = new HashMap<String, String>();
            	map.put("ec", "200");
            	map.put("em", "action not exists");
            	String cmd = JSON.toJSON(map).toString();
            	String redis_cmd = this.bulidRedisString(cmd);
                ctx.writeAndFlush(this.buildRespBody(redis_cmd));
            } else {
            	System.out.println("action:" + action);
            	Api api_obj = api.get(action);
            	
            	String interClass = api_obj.getInterClass();
            	
            	System.out.println("interClass:" + interClass);
            	
            	Class<?> class_obj = Class.forName(interClass);
            	
            	System.out.println("class_obj:" + class_obj.getName());
            	
            	Object api_class = class_obj.newInstance();
            	
            	Method api_method = class_obj.getMethod(method);
            	System.out.println(api_method);
            	
            	System.out.println("args:" + args);
            	Object ret_obj = api_method.invoke(api_class);
            	
		        String ret = ret_obj.toString();
		        
		        // 返回结果
		        String redis_cmd = this.bulidRedisString(ret);
		        
		        ctx.writeAndFlush(this.buildRespBody(redis_cmd));
            }
        }
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
