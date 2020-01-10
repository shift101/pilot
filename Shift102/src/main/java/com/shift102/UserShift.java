package com.shift102;

public class UserShift {

	private int user_id;
	private int shift_id;
	private String user_name;
	private String shift_name;
	private String shift_time;
	private int[] weekoff;
	private int[] leave;
	private int[] unplannedLeave;
	
	public int[] getUnplannedLeave() {
		return unplannedLeave;
	}

	public void setUnplannedLeave(int[] unplannedLeave) {
		this.unplannedLeave = unplannedLeave;
	}

	public UserShift() {
		super();
	};
	
	public UserShift(int user_id, int shift_id, String user_name, String shift_name, String shift_time, int[] weekoff,
			int[] leave,int[] unplannedLeave) {
		super();
		this.user_id = user_id;
		this.shift_id = shift_id;
		this.user_name = user_name;
		this.shift_name = shift_name;
		this.shift_time = shift_time;
		this.weekoff = weekoff;
		this.leave = leave;
		this.unplannedLeave = unplannedLeave;
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
	public int[] getWeekoff() {
		return weekoff;
	}
	public void setWeekoff(int[] weekoff) {
		this.weekoff = weekoff;
	}
	public int[] getLeave() {
		return leave;
	}
	public void setLeave(int[] leave) {
		this.leave = leave;
	}
}
