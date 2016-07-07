package com.rpc.main;

import java.util.Map;

import com.rpc.utils.Config;

/**
 * Created by ruansheng on 16/7/1.
 */
public class Server {
	
    public static void main(String[] args) throws Exception {
    	String filePath = "res/server.properties";
    	Map<String, Object> config = new Config().getServerConfig(filePath);
    	if(config == null) {
    		System.out.println("get server config ");
    	} else {
    		String serverName = config.get("serverName").toString();
        	int port = Integer.parseInt(config.get("port").toString());
        	System.out.println("server " + serverName + " listen port " + port +" is running...");
        	new Connector().bind(port);
    	}
    }
}
