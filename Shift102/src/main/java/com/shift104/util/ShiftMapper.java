package com.shift104.util;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;

import com.shift104.model.ExceptionData;
import com.shift104.model.Shift;
import com.shift104.model.UserShiftData;

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
				// userShift.setData_id(srs.getInt("DATA_ID"));
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
}
