package com.rpc.utils;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Config {
	
	/**
	 * get server config
	 * @param filePath
	 * @return
	 */
	public Map<String, Object> getServerConfig(String filePath) {
		Map<String, Object> config = new HashMap<String, Object>();
		InputStream in = null;
		try {
			in = new BufferedInputStream(new FileInputStream(filePath));
			Properties prop = new Properties();
			prop.load(in);
			
			String serverName = prop.getProperty("serverName").trim();   
            String port = prop.getProperty("port").trim();
            config.put("serverName", serverName);
            config.put("port", port);
		} catch (Exception e) {
		    config = null;
		    System.out.println(e.getMessage());
		}
        
		return config;
	}
}
