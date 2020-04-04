package com.shift105.model;

public class Exception {
	
	public Exception(int excp_id,String excp_name,String excp_plan){
		this.excp_id=excp_id;
		this.excp_name=excp_name;
		this.excp_plan=excp_plan;
	}

	private int excp_id;
	private String excp_name;
	private String excp_plan;
	
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
	public String getExcp_plan() {
		return excp_plan;
	}
	public void setExcp_plan(String excp_plan) {
		this.excp_plan = excp_plan;
	}
}
