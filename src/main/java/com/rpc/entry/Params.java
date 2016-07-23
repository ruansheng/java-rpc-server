package com.rpc.entry;

public class Params {

	private String m;
	
	private Object[] args;

	private Class<?>[] types;
	
	public String getM() {
		return m;
	}

	public void setM(String m) {
		this.m = m;
	}

	public Object[] getArgs() {
		return args;
	}

	public void setArgs(Object[] args) {
		this.args = args;
	}

	public Class<?>[] getTypes() {
		return types;
	}

	public void setTypes(Class<?>[] types) {
		this.types = types;
	}	
	
}
