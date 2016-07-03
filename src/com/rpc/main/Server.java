package com.rpc.main;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.rpc.utils.JsonFile;

/**
 * Created by ruansheng on 16/7/1.
 */
public class Server {

    public static void main(String[] args) throws Exception {
        int port = 8080;
        new Connector().bind(port);
    	String filepath="res/api.json";
    	String data = JsonFile.ReadFile(filepath); 
    	JSONArray obj = JsonFile.toJSONArray(data);
        System.out.println(obj);
    }
}
