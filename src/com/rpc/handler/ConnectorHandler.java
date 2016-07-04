package com.rpc.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rpc.entry.Api;
import com.rpc.utils.JsonFile;
import com.rpc.utils.StringToArray;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashMap;
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
        if(strings.length == 5) {
        	String params = strings[4];
            
            JSONObject json_obj = JSON.parseObject(params);
            JSONObject params_obj = (JSONObject)json_obj.get("params");
            
            // 解析出参数
            String action = (String) json_obj.get("action");
            String method_name = (String) params_obj.get("m");
            JSONArray args_array = params_obj.getJSONArray("args");
            
            System.out.println("action:" + action);
            System.out.println("method_name:" + method_name);
            System.out.println("args_array:" + args_array.toJSONString());
            
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
            	
            	Method[] api_methods = class_obj.getMethods();
            	
            	Method api_method = null;
            	for(int i = 0; i < api_methods.length; i++) {
            		if(api_methods[i].getName().equals(method_name)) {
            			api_method = api_methods[i]; 
            			break;
            		}
            	}
            	
            	Type[] types = api_method.getGenericParameterTypes();
            	
            	System.out.println("length:" + types.length);
            	
            	// 判断客户端传的参数个数和服务端定义参数个数是否一致
            	if(types.length != args_array.size()) {
            		Map<String, String> map = new HashMap<String, String>();
                	map.put("ec", "401");
                	map.put("em", "client params != server params");
                	String cmd = JSON.toJSON(map).toString();
                	String redis_cmd = this.bulidRedisString(cmd);
                    ctx.writeAndFlush(this.buildRespBody(redis_cmd));
                    return ;
            	}
            	
            	Object object[] = new Object[]{};
            	for(int i = 0; i < types.length; i++) {
            		String param_class_name = args_array.get(i).getClass().getName();
            		
            		System.out.println("1" + args_array.get(i).getClass().getName());
            		System.out.println("2" + types[i].getClass().getName());
            		//Class c = types[i].getClass();
            		//Object cs = c.newInstance();
            		
            		//System.out.println(args_array.get(i));
            	}
            	/*
            	for(int j = 0; j < object.length; j++) {
            		System.out.println(object.toString());
            	}
            	*/
            	
            	/*
            	String s = new String("hello");
            	Object ret_obj = api_method.invoke(api_class, s);
            	
		        String ret = ret_obj.toString();
		        */
            	String ret = "hello";
		        // 返回结果
		        String redis_cmd = this.bulidRedisString(ret);
		        
		        ctx.writeAndFlush(this.buildRespBody(redis_cmd));
            
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
