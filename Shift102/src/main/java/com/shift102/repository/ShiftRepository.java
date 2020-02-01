package com.shift102.repository;

import java.time.Month;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.shift102.model.Shift;
import com.shift102.model.ShiftStat;
import com.shift102.model.UserStat;
import com.shift102.model.UserShiftData;

@Repository
public class ShiftRepository{
	
	@Autowired
    private JdbcTemplate jdbcTemplate;
	
	public Shift getShiftByMonthYear() {
		
		return this.getShiftByMonthYear(null, null);
	}

	public Shift getShiftByMonthYear(String month, String year) {
		if(month == null || year == null) {
			java.util.Date date= new Date();
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			month = String.valueOf(cal.get(Calendar.MONTH)+1);
			year = String.valueOf(cal.get(Calendar.YEAR));
		}
		SqlRowSet srs = jdbcTemplate.queryForRowSet
				(
						"select cal.month,cal.year,data.lastupdated,data.lastupdatedby,exp.id EXCEPTION_ID, " + 
						"GROUP_CONCAT(date) exceptions, " + 
						"user.name,user.id USER_ID, " + 
						"exp.exceptionname,shift.shiftname,shift.id SHIFT_ID, " + 
						"shift.shiftstart,shift.shiftend,shift.timezone " + 
						"from calendar cal " + 
						"join shiftdata data on calendar_id=cal.id " + 
						"join userdata user on user.id=data.user_id " + 
						"join shifts shift on shift.id=data.shift_id " + 
						"join EXCEPTIONDATA edata on edata.shift_id=data.id " + 
						"join exceptiontypes exp on exp.id=edata.exception_id " + 
						"where cal.month="+month+" and cal.year="+year+" " + 
						"group by cal.month,cal.year,user.name, " + 
						"exp.exceptionname,shift.shiftname,shift.shiftstart,shift.shiftend,shift.timezone,data.lastupdated,data.lastupdatedby,user.id,shift.id,exp.id"
						);
		
		UserShiftData userShift=null;
		List<UserShiftData> list =new ArrayList<UserShiftData>();
		Shift shift= new Shift();
		int userId=-1;
		shift.setMonth_id(Integer.parseInt(month));
		shift.setYear(Integer.parseInt(year));
		shift.setMonth_name(Month.of(Integer.parseInt(month)).name());
		
		while (srs.next()) {
			if(userId == srs.getInt("USER_ID")) {
				setExceptions(srs, userShift);
			}else {
				if(userId!=-1)list.add(userShift);
				userShift = new UserShiftData();
					userShift.setShift_name(srs.getString("shiftname"));
					userShift.setUser_name(srs.getString("name"));
					userShift.setShift_id(srs.getInt("SHIFT_ID"));
					userShift.setUser_id(srs.getInt("USER_ID"));
					userShift.setShift_time(srs.getString("shiftstart")
					+"-"+srs.getString("shiftend")+" "+srs.getString("timezone"));
					setExceptions(srs, userShift);
			}
			userId = srs.getInt("USER_ID");
			
			if(srs.isLast())list.add(userShift);
			
		}
		shift.setUsershift(list);
		return shift;
	}

	private void setExceptions(SqlRowSet srs, UserShiftData userShift) {
		switch(srs.getInt("EXCEPTION_ID")) {
		case 0:{
			if (userShift.getWeekoff()==null) userShift.setWeekoff(getIntArrayFromString(srs.getString("exceptions"))); //Weekoff
			//System.out.println("Inside weekoff");
			break;
		}
		case 1:{
			if (userShift.getLeave()==null) userShift.setLeave(getIntArrayFromString(srs.getString("exceptions"))); //Planned Leaves
			//System.out.println("Inside Planned Leaves");
			break;
		}
		case 2:{
			if (userShift.getUnplannedLeave()==null) userShift.setUnplannedLeave(getIntArrayFromString(srs.getString("exceptions"))); //Unplanned Leaves
			//System.out.println("Inside Unplanned Leaves");
			break;
		}
		}
	}
	
	private int[] getIntArrayFromString(String str) {
		String[] strArr=str.split(",");
		int[] arr =new int[strArr.length];
		int i=0;
	    for(String strI:strArr){
	        arr[i]=Integer.parseInt(strI.trim());//Exception in this line
	        i++;
	    }
	    return arr;
	}

	
	public List<Integer> getYears() {
		SqlRowSet srs = jdbcTemplate.queryForRowSet("select distinct year from calendar order by year");
		List<Integer> years = new ArrayList<Integer>();
		int year =0;
		while (srs.next()) {
			year=srs.getInt("year");
			years.add(year);
			
			}
		if(!years.contains(Calendar.getInstance().get(Calendar.YEAR))) {
			years.add(Calendar.getInstance().get(Calendar.YEAR));
		}
		return years;
	}


	public List<UserStat> getAllUsers() {
		SqlRowSet srs = jdbcTemplate.queryForRowSet("select id,name from userdata");
		List<UserStat> users = new ArrayList<UserStat>();
		UserStat user;
		while (srs.next()) {
			user=new UserStat();
			user.setUserId(Integer.toString(srs.getInt("ID")));
			user.setUserName(srs.getString("NAME"));
			
			users.add(user);
			}

		return users;
	}

	public List<ShiftStat> getAllShifts() {
		SqlRowSet srs = jdbcTemplate.queryForRowSet("select * from shifts");
		List<ShiftStat> shifts = new ArrayList<ShiftStat>();
		ShiftStat shift;
		while (srs.next()) {
			shift=new ShiftStat();
			shift.setShift_id((srs.getInt("ID")));
			shift.setShift_name((srs.getString("SHIFTNAME")));
			shift.setShift_time(((srs.getString("SHIFTSTART")
					.concat("-").concat(srs.getString("SHIFTEND")
							.concat(" ").concat(srs.getString("TIMEZONE"))))));
			
			shifts.add(shift);
			}

		return shifts;
	}

}
