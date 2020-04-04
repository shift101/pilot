package com.shift105.model;

public class Attendance {
private String date;
private String updatedby;
private int emp_id;
private double punch_time;

public String getDate() {
	return date;
}
public void setDate(String date) {
	this.date = date;
}
public String getUpdatedby() {
	return updatedby;
}
public void setUpdatedby(String updatedby) {
	this.updatedby = updatedby;
}
public int getEmp_id() {
	return emp_id;
}
public void setEmp_id(int emp_id) {
	this.emp_id = emp_id;
}
public double getPunch_time() {
	return punch_time;
}
public void setPunch_time(double punch_time) {
	this.punch_time = punch_time;
}

}
