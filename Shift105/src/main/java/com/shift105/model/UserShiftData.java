package com.shift105.model;

import java.util.List;

public class UserShiftData {

	private int user_id;
	private int shift_id;
	private String user_name;
	private String shift_name;
	private String shift_time;
	private List<ExceptionData> exceptionData;
	private List<DayProp> excelData;
	private char typeOfData;
	
	public char getTypeOfData() {
		return typeOfData;
	}

	public void setTypeOfData(char typeOfData) {
		this.typeOfData = typeOfData;
	}

	public List<DayProp> getExcelData() {
		return excelData;
	}

	public void setExcelData(List<DayProp> excelData) {
		this.excelData = excelData;
	}

	public List<ExceptionData> getExceptionData() {
		return exceptionData;
	}

	public void setExceptionData(List<ExceptionData> exceptionData) {
		this.exceptionData = exceptionData;
	}

	public UserShiftData() {
		super();
	};
	
	public UserShiftData(int user_id, int shift_id, String user_name, String shift_name, String shift_time, ExceptionData weekoff,
			ExceptionData leave,ExceptionData unplannedLeave, ExceptionData specialLeave) {
		super();
		this.user_id = user_id;
		this.shift_id = shift_id;
		this.user_name = user_name;
		this.shift_name = shift_name;
		this.shift_time = shift_time;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public int getShift_id() {
		return shift_id;
	}
	public void setShift_id(int shift_id) {
		this.shift_id = shift_id;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getShift_name() {
		return shift_name;
	}
	public void setShift_name(String shift_name) {
		this.shift_name = shift_name;
	}
	public String getShift_time() {
		return shift_time;
	}
	public void setShift_time(String shift_time) {
		this.shift_time = shift_time;
	}
}
