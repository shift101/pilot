package com.shift104.model;

import java.util.List;

public class Shift {
	
	public Shift() {
		super();
	}
	public Shift(int month_id, String month_name, int year, List<UserShiftData> usershift) {
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
	public List<UserShiftData> getUsershift() {
		return usershift;
	}
	public void setUsershift(List<UserShiftData> usershift) {
		this.usershift = usershift;
	}
	/**
	 {
		  "month_id": 12,
		  "month_name": "DECEMBER",
		  "year": 2019,
		  "usershift": [
		    {
		      "user_id": 0,
		      "shift_id": 1,
		      "user_name": "Himanshu Sharma",
		      "shift_name": "General",
		      "shift_time": "0800-1630 IST",
		      "weekoff": [2,3,9,10,16,17,23,24,30,31],
		      "leave": [4,13],
		      "unplannedLeave": null
		    },
		    {
		      "user_id": 1,
		      "shift_id": 2,
		      "user_name": "Deepak Singh",
		      "shift_name": "UK",
		      "shift_time": "1430-2230 IST",
		      "weekoff": [3,4,10,11,17,18,24,25,31],
		      "leave": null,
		      "unplannedLeave": [6,13]
		    }
		  ]
		}
	 */

	private int month_id;
	private String calendar_id;
	public String getCalendar_id() {
		return calendar_id;
	}
	public void setCalendar_id(String calendar_id) {
		this.calendar_id = calendar_id;
	}
	private String month_name;
	private int year;
	private List<UserShiftData> usershift;

	// getters and setters
	/*
	 * @Override public String toString() { return "Employee [id=" + id +
	 * ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email +
	 * "]"; }
	 */

}
