package com.rpc.inter;

import java.util.Map;

public interface TestInterFace {

	public String getKey(String uid, Integer age);
	
	public int getVal(String uid, Integer age);
	
	public Map<String, String> getUserInfo(String uid, Integer age);

}
