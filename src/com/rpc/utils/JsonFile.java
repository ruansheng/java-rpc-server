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
	public static String ReadFile(String filepath){
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
	
	public static JSONArray toJSONArray(String data){
		JSONArray obj = JSON.parseArray(data);
		return obj;
	}
	
	public static HashMap<String, Api> toApiProto(JSONArray jsonarray) {
		HashMap<String, HashMap<String, String>> hm = new HashMap<String, HashMap<String, String>>();
		for(int i = 0; i < jsonarray.size(); i++) {
			System.out.println("");
		}
		return null; 
	}
}
