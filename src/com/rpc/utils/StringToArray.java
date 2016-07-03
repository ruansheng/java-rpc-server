package com.rpc.utils;

public class StringToArray {

	public static String[] convertStrToArray(String str){   
        String[] strArray = null;   
        strArray = str.split("\r\n"); 
        return strArray;
    }   
	
}
