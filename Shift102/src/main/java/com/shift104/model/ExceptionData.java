package com.shift104.model;

public class ExceptionData {

	public String getData_Id() {
		return data_id;
	}
	public void setData_Id(String data_id) {
		this.data_id = data_id;
	}
	public Integer[] getDates() {
		return dates;
	}
	public void setDates(Integer[] dates) {
		this.dates = dates;
	}
	private String data_id;
	private Integer[] dates;
	private String excp_id;
	private String excp_name;
	public String getExcp_name() {
		return excp_name;
	}
	public void setExcp_name(String excp_name) {
		this.excp_name = excp_name;
	}
	public String getExcp_id() {
		return excp_id;
	}
	public void setExcp_id(String excp_id) {
		this.excp_id = excp_id;
	}
}
