package com.rpc.impl;

import java.util.HashMap;
import java.util.Map;

import com.rpc.inter.*;

public class Test implements TestInterFace{

	public String getKey(String uid, Integer age) {
		return "ruansheng";
	}
	
	public int getVal(String uid, Integer age) {
		return 10;
	}
	
	public Map<String, String> getUserInfo(String uid, Integer age) {
		Map<String, String> userinfo = new HashMap<String, String>();
		userinfo.put("name", "ruansheng");
		userinfo.put("sex", "M");
		return userinfo;
	}

}