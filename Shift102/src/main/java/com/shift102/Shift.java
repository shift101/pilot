package com.shift102;

import java.util.List;

public class Shift {
	
	public Shift() {
		super();
	}
	public Shift(int month_id, String month_name, int year, List<UserShift> usershift) {
		super();
		this.month_id = month_id;
		this.month_name = month_name;
		this.year = year;
		this.usershift = usershift;
	}
	public int getMonth_id() {
		return month_id;
	}
	public void setMonth_id(int month_id) {
		this.month_id = month_id;
	}
	public String getMonth_name() {
		return month_name;
	}
	public void setMonth_name(String month_name) {
		this.month_name = month_name;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public List<UserShift> getUsershift() {
		return usershift;
	}
	public void setUsershift(List<UserShift> usershift) {
		this.usershift = usershift;
	}
	/**
	 {"shift":
	{
		"month_id": "12",
		"month_name": "December",
		"year": "2019",
		"usershift": [
		{
			"user_id": "1",
			"shift_id":"1",
			"user_name": "Himanshu Sharma",
			"shift_name":"General",
			"shift_time":"0800-1630 IST",
			"weekoff": [2,3,9,10,16,17,23,24,30,31],
			"Leave": [4,19]
		},
		{
			"user_id": "2",
			"shift_id":"2",
			"user_name": "Deepak Singh",
			"shift_name":"UK",
			"shift_time":"0800-1630 IST",
			"weekoff": [4,5,11,12,18,19,25,26],
			"Leave": [2,15]
		}
		]
	}
}
	 */

	private int month_id;
	private String month_name;
	private int year;
	private List<UserShift> usershift;

	// getters and setters
	/*
	 * @Override public String toString() { return "Employee [id=" + id +
	 * ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email +
	 * "]"; }
	 */
}
