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
import com.shift102.model.UserShift;

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
				("select " + 
						"GROUP_CONCAT(exceptiondate) exceptions," + 
						"user.name,user.id USER_ID," + 
						"exp.exceptionname exceptionType,shift.shiftname,shift.id SHIFT_ID," + 
						"shift.shiftstart,shift.shiftend,shift.timezone,exception_id " + 
						"from calendar cal " + 
						"join shiftdata data on calendar_id=cal.id " + 
						"join userdata user on user.id=data.user_id " + 
						"join exceptiontypes exp on exp.id=data.exception_id " + 
						"join shifts shift on shift.id=data.shift_id " + 
						"where cal.month="+month+" and cal.year="+year+" "+
						"group by " + 
						"user.name," + 
						"exp.exceptionname,shift.shiftname,shift.shiftstart,shift.shiftend,shift.timezone," + 
						"user.id,shift.id,exception_id "
						+ "order by USER_ID");
		
		UserShift userShift=null;
		List<UserShift> list =new ArrayList<UserShift>();
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
				userShift = new UserShift();
				//if (userShift.getShift_name()==null) 
					userShift.setShift_name(srs.getString("shiftname"));
				//if (userShift.getUser_name()==null) 
					userShift.setUser_name(srs.getString("name"));
					userShift.setShift_id(srs.getInt("SHIFT_ID"));
					userShift.setUser_id(srs.getInt("USER_ID"));
				//if (userShift.getShift_time()==null) 
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

	private void setExceptions(SqlRowSet srs, UserShift userShift) {
		switch(srs.getString("exception_id")) {
		case "0":{
			if (userShift.getWeekoff()==null) userShift.setWeekoff(getIntArrayFromString(srs.getString("exceptions"))); //Weekoff
			//System.out.println("Inside weekoff");
			break;
		}
		case "1":{
			if (userShift.getLeave()==null) userShift.setLeave(getIntArrayFromString(srs.getString("exceptions"))); //Planned Leaves
			//System.out.println("Inside Planned Leaves");
			break;
		}
		case "2":{
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

}
