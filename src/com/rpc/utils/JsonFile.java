package com.rpc.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.HashMap;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rpc.entry.Api;

public class JsonFile {
	
	/**
	 * ReadFile
	 * @param path
	 * @return
	 */
	private static String ReadFile(String filepath){
	    File file = new File(filepath);
	    BufferedReader reader = null;
	    String data = "";
	    try {
	    	reader = new BufferedReader(new FileReader(file));
	    	String tempString = null;
		    while ((tempString = reader.readLine()) != null) {
		    	data = data + tempString;
		    }
		    reader.close();
	    } catch (IOException e) {
	    	e.printStackTrace();
	    } finally {
		     if (reader != null) {
			      try {
			    	  reader.close();
			      } catch (IOException e1) {
			      
			      }
		     }
	    }
	    return data;
	}
	
	private static JSONArray toJSONArray(String data){
		JSONArray obj = JSON.parseArray(data);
		return obj;
	}
	
	/**
	 * json file -> HashMap<String, Api>
	 * @param filepath
	 * @return
	 */
	public static HashMap<String, Api> toApiProto(String filepath) {
		String data = ReadFile(filepath);
		JSONArray jsonarray = toJSONArray(data);
		HashMap<String, Api> hm = new HashMap<String, Api>();
		for(int i = 0; i < jsonarray.size(); i++) {
			Api tmp = JSON.parseObject(jsonarray.get(i).toString(), Api.class);
			hm.put(tmp.getServiceUri(), tmp);
		}
		return hm; 
	}

	
}
