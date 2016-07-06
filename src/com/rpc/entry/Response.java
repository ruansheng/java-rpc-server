package com.rpc.entry;

public class Response {

	private String id;
	
	private int ec;
	
	private String em;
	
	private Object result;

	public Response(String id, int ec, String em) {
		this.id = id;
		this.ec = ec;
		this.em = em;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getEc() {
		return ec;
	}

	public void setEc(int ec) {
		this.ec = ec;
	}

	public String getEm() {
		return em;
	}

	public void setEm(String em) {
		this.em = em;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}
}
