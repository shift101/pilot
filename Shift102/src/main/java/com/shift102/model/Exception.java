package com.shift102.model;

public class Exception {
	
	public Exception(int excp_id,String excp_name){
		this.excp_id=excp_id;
		this.excp_name=excp_name;
	}

	private int excp_id;
	private String excp_name;
	
	public int getExcp_id() {
		return excp_id;
	}
	public void setExcp_id(int excp_id) {
		this.excp_id = excp_id;
	}
	public String getExcp_name() {
		return excp_name;
	}
	public void setExcp_name(String excp_name) {
		this.excp_name = excp_name;
	}
}
