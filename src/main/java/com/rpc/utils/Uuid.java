package com.rpc.utils;

import java.util.UUID;

public class Uuid {

   public static String getUUID() {
	   return UUID.randomUUID().toString().replace("-","");
   }
}
