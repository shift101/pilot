package com.shift105.model;

import java.util.List;

public class AttendanceVariance {

	private int emp_id;
	private int user_id;
	private String user_name;
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	private List<PunchTimeDeficit> timeDeficitList;
	public int getEmp_id() {
		return emp_id;
	}
	public void setEmp_id(int emp_id) {
		this.emp_id = emp_id;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public List<PunchTimeDeficit> getTimeDeficitList() {
		return timeDeficitList;
	}
	public void setTimeDeficitList(List<PunchTimeDeficit> timeDeficitList) {
		this.timeDeficitList = timeDeficitList;
	}
}
