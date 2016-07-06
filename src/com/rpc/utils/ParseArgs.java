package com.rpc.utils;

import com.alibaba.fastjson.JSON;
import com.rpc.entry.Body;
import com.rpc.entry.Request;

public class ParseArgs {

	public static Request toRequest(String body) {
		Body bobj = JSON.parseObject(body, Body.class);
		
		Object[] os = bobj.getParams().getArgs();
    	
    	Class<?>[] types = Types.getTypes(os);
    	
    	bobj.getParams().setTypes(types);
    	
    	// 封装请求
    	Request request = new Request();
    	
    	request.setId(bobj.getId());
    	request.setAction(bobj.getAction());
    	request.setM(bobj.getParams().getM());
    	request.setObjects(bobj.getParams().getArgs());
    	request.setTypes(bobj.getParams().getTypes());
    	
		return request;
	}
	
}
