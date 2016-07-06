package com.rpc.entry;

public class Request {

	private String id;
	
	private String action;
	
	private String m;
	
	private Object[] objects;
	
	private Class<?>[] types;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Object[] getObjects() {
		return objects;
	}

	public void setObjects(Object[] objects) {
		this.objects = objects;
	}

	public Class<?>[] getTypes() {
		return types;
	}

	public void setTypes(Class<?>[] types) {
		this.types = types;
	}

	public String getM() {
		return m;
	}

	public void setM(String m) {
		this.m = m;
	}
	
}
