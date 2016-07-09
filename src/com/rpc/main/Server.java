package com.rpc.main;

import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.rpc.utils.Config;

/**
 * Created by ruansheng on 16/7/1.
 */
public class Server {

    public static void main(String[] args) throws Exception {
    	Logger logger = null;
    	try {
    		logger = Logger.getLogger(Server.class);
    		PropertyConfigurator.configure("res/log4j.properties");
    		String filePath = "res/server.properties";
        	Map<String, Object> config = new Config().getServerConfig(filePath);
        	if(config == null) {
        		logger.info("get server config is fail");
        	} else {
        		String serverName = config.get("serverName").toString();
            	int port = Integer.parseInt(config.get("port").toString());
            	logger.info("server " + serverName + " listen port " + port +" is running...");
            	new Connector().bind(port);
        	}
    	} catch (Exception e) {
    		System.out.println(e.getMessage());
    	}
    }
}
