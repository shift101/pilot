package com.shift105.util;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;

import com.shift105.model.AttendanceVariance;
import com.shift105.model.DayProp;
import com.shift105.model.ExceptionData;
import com.shift105.model.PunchTimeDeficit;
import com.shift105.model.Shift;
import com.shift105.model.UserShiftData;

@Service
public class ShiftMapper {

	public Shift mapShift(SqlRowSet srs,int month,int year) throws Exception {
		UserShiftData userShift = null;
		List<UserShiftData> list = new ArrayList<UserShiftData>();
		Shift shift = new Shift();
		int userId = -1;

		shift.setMonth_id(month);
		shift.setYear(year);
		shift.setMonth_name(Month.of(month).name());

		while (srs.next()) {
			if (userId == srs.getInt("USER_ID")) {
				setExceptions(srs, userShift);
			} else {
				if (userId != -1)
					list.add(userShift);
				userShift = new UserShiftData();
				userShift.setShift_name(srs.getString("SHIFT_NAME"));
				userShift.setUser_name(srs.getString("USER_NAME"));
				userShift.setShift_id(srs.getInt("SHIFT_ID"));
				userShift.setUser_id(srs.getInt("USER_ID"));
				userShift.setShift_time(srs.getString("SHIFT_START") + "-" + srs.getString("SHIFT_END") + " "
						+ srs.getString("SHIFT_TIMEZONE"));
				setExceptions(srs, userShift);
			}
			userId = srs.getInt("USER_ID");

			if (srs.isLast())
				list.add(userShift);

		}
		shift.setUsershift(list);
		return shift;
	}
	
	public Shift mapShiftNew(SqlRowSet srs,int month,int year) throws Exception {
		Calendar cal = Calendar.getInstance();
	    cal.set(Calendar.MONTH, month-1);
	    cal.set(Calendar.YEAR,year);	    
	    int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
	    
	    List<UserShiftData> list = new ArrayList<UserShiftData>();
	    Shift shift = new Shift();
	    UserShiftData usexcel = null;
	    
	    while (srs.next()) {
	    	usexcel = new UserShiftData();
	    	usexcel.setUser_name(srs.getString("USER_NAME"));
	    	usexcel.setShift_name(srs.getString("SHIFT_NAME"));
	    	usexcel.setUser_id(srs.getInt("USER_ID"));
	    	usexcel.setShift_id(srs.getInt("SHIFT_ID"));
	    	usexcel.setTypeOfData(srs.getString("TYPE_OF_DATA").charAt(0));
	    	usexcel.setExcelData(arrangeDayList(srs.getString("DATES"),maxDay));
	    	list.add(usexcel);
	    }
	    shift.setUsershift(list);
	    shift.setMonth_id(month);
	    shift.setYear(year);
		return shift;
	}

	private List<DayProp> arrangeDayList(String string,int maxDay) {
		ArrayList<DayProp> list = new ArrayList<DayProp>();
		DayProp dayProp = null;
		for (int i = 1; i <= maxDay; i++) {
			dayProp = new DayProp();
			dayProp.setDay(i);
			dayProp.setExcp_code("WD");
			
			for (String str : string.split("-")) {
				String[] sep = str.split("~");
				if(Arrays.binarySearch(toIntArray(sep[1].replace(" ", ""),","), i) > -1) {
					dayProp.setDay(i);
					dayProp.setExcp_code(sep[0]);
					break;
				}
			}
			list.add(dayProp);
		}
		
		return list;
	}
	public static int[] toIntArray(String input, String delimiter) {

		return Arrays.stream(input.split(delimiter)).mapToInt(Integer::parseInt).toArray();
	}

	private void setExceptions(SqlRowSet srs, UserShiftData userShift) throws Exception{
		ExceptionData exception = new ExceptionData();
		exception.setData_Id(srs.getString("DATA_ID"));
		exception.setDates(getIntArrayFromString(srs.getString("DATA_DATES")));
		exception.setExcp_id(srs.getString("EXCP_ID"));
		exception.setExcp_name(getExceptionName(srs.getString("EXCP_ID")));
		if(userShift.getExceptionData()==null) {
			ArrayList<ExceptionData> list = new ArrayList<ExceptionData>();
			list.add(exception);
			userShift.setExceptionData(list);
		}else {
			userShift.getExceptionData().add(exception);
		}
			
	}

	private String getExceptionName(String string) {
		switch(string) {
		case "0":return "WO";
		case "1":return "PL";
		case "2":return "UL";
		case "3":return "SL";
		default: return "WD";
		
		}
	}

	private Integer[] getIntArrayFromString(String str) {
		String[] strArr = str.split(",");
		Integer[] arr = new Integer[strArr.length];
		int i = 0;
		for (String strI : strArr) {
			arr[i] = Integer.parseInt(strI.trim());// Exception in this line
			i++;
		}
		return arr;
	}

	public List<DayProp> mapUserShift(SqlRowSet srs) {
		List<DayProp> userShift=new ArrayList<DayProp>();
		while (srs.next()) {
			
		}
		return userShift;
	}

	public List<AttendanceVariance> mapTimeDeficit(SqlRowSet srs) {
		List<AttendanceVariance> shift = new ArrayList<AttendanceVariance>();
		AttendanceVariance var=null;
		int user=-1;
		while (srs.next()) {
			if(user != srs.getInt("USER_ID")) {
				var=new AttendanceVariance();
				var.setEmp_id(srs.getInt("ATT_EMP_ID"));
				var.setUser_id(srs.getInt("USER_ID"));
				var.setUser_name(srs.getString("USER_NAME"));
				shift.add(var);
				user=srs.getInt("USER_ID");
			}
			setPunchTimeDeficit(var,srs);
		}
		return shift;
	}

	private void setPunchTimeDeficit(AttendanceVariance var,SqlRowSet srs) {
		PunchTimeDeficit ptd = new PunchTimeDeficit();
		ptd.setDate(srs.getInt("ATT_DATE"));
		ptd.setTimeDeficit(srs.getDouble("ATT_PUNCHTIME"));
		if(var.getTimeDeficitList() == null) {
			ArrayList<PunchTimeDeficit> list = new ArrayList<PunchTimeDeficit>();
			list.add(ptd);
			var.setTimeDeficitList(list);
		}else {
			var.getTimeDeficitList().add(ptd);
		}
		
		
	}
}
