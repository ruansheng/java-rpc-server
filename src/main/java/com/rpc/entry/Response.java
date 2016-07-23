package com.rpc.entry;

public class Response {

	private String id;
	
	private int en;
	
	private String em;
	
	private Object result;

	public Response(String id, int en, String em) {
		this.id = id;
		this.en = en;
		this.em = em;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getEn() {
		return en;
	}

	public void setEn(int en) {
		this.en = en;
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
