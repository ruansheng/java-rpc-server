package com.rpc.utils;

public class Types {
	
	public static Class<?>[] getTypes(Object[] objs) {
		Class<?>[] types = new Class[objs.length];
		for(int i = 0; i < objs.length; i++) {
			types[i] = objs[i].getClass();
    	}
    	return types;
	}
}
