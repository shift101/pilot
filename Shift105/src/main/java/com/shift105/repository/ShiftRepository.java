package com.shift105.repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.shift105.model.Attendance;
import com.shift105.model.AttendanceVariance;
import com.shift105.model.DayProp;
import com.shift105.model.ExceptionData;
import com.shift105.model.Shift;
import com.shift105.model.UserShiftData;
import com.shift105.util.ShiftMapper;

@Repository
public class ShiftRepository extends RepositoryCommon{

	
	@Autowired
	private ShiftMapper mapper;
	
	private final int INSERT_BATCH_SIZE = 1;
	private final int UPDATE_BATCH_SIZE = 1;
	private final String insertQuery="INSERT INTO SHIFTDATA_PLANNED(DATA_CALENDAR_ID, DATA_SHIFT_ID, DATA_EXCEPTION_ID, "
			+ "DATA_USER_ID, DATA_DATES, DATA_LASTUPDATED, DATA_LASTUPDATEDBY) VALUES(?,?,?,?,?,?,?)";
	private final String insertActualQuery="INSERT INTO SHIFTDATA_ACTUAL(DATA_CALENDAR_ID, DATA_SHIFT_ID, DATA_EXCEPTION_ID, "
			+ "DATA_USER_ID, DATA_DATES, DATA_LASTUPDATED, DATA_LASTUPDATEDBY) VALUES(?,?,?,?,?,?,?)";
	private final String updateQuery="UPDATE SHIFTDATA_PLANNED set DATA_CALENDAR_ID=?, DATA_SHIFT_ID=?, DATA_EXCEPTION_ID=?, "
			+ "DATA_USER_ID=?, DATA_DATES=?, DATA_LASTUPDATED=?, DATA_LASTUPDATEDBY=? where DATA_ID=?";
	private final String updateActualQuery="UPDATE SHIFTDATA_ACTUAL set DATA_CALENDAR_ID=?, DATA_SHIFT_ID=?, DATA_EXCEPTION_ID=?, "
			+ "DATA_USER_ID=?, DATA_DATES=?, DATA_LASTUPDATED=?, DATA_LASTUPDATEDBY=? where DATA_ID=?";
	private final String attMergeQuery = "MERGE INTO ATTENDANCE  USING (VALUES(?,?,?,?,?,?)) " + 
			"   AS VALS(A,B,C,D,E,F) ON ATTENDANCE.ATT_DATE = VALS.A AND ATTENDANCE.ATT_EMP_ID=VALS.B " + 
			"   WHEN MATCHED THEN UPDATE SET ATTENDANCE.ATT_PUNCHTIME = VALS.C,ATTENDANCE.ATT_LASTUPDATED=VALS.D, ATTENDANCE.ATT_LASTUPDATEDBY=VALS.E,ATTENDANCE.ATT_CALENDAR_ID=VALS.F " + 
			"   WHEN NOT MATCHED THEN INSERT(ATT_DATE,ATT_EMP_ID,ATT_PUNCHTIME,ATT_CALENDAR_ID,ATT_LASTUPDATED,ATT_LASTUPDATEDBY) VALUES (VALS.A,VALS.B,VALS.C,VALS.F,VALS.D,VALS.E)";
	private final String getCalIdQuery = "SELECT CAL_ID FROM CALENDAR WHERE CAL_MONTH=? AND CAL_YEAR=?";

	public Shift getShiftByMonthYear() {

		return this.getShiftByMonthYear(null, null);
	}

	public Shift getShiftByMonthYear(String month, String year) {
		Shift shift=null;
		if (month == null || year == null) {
			java.util.Date date = new Date();
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			month = String.valueOf(cal.get(Calendar.MONTH) + 1);
			year = String.valueOf(cal.get(Calendar.YEAR));
		}
		SqlRowSet srs;
		try {
			srs = jdbcTemplate
					.queryForRowSet("SELECT DATA_ID,CAL_MONTH,CAL_YEAR,SHIFT_ID,SHIFT_NAME,SHIFT_START,SHIFT_END,SHIFT_TIMEZONE, "
							+ "USER_ID,USER_NAME,EXCP_ID,EXCP_EXCEPTIONNAME, " + "DATA_DATES " + "FROM SHIFTDATA_PLANNED "
							+ "JOIN CALENDAR ON CAL_ID=DATA_CALENDAR_ID " + "JOIN SHIFTS ON SHIFT_ID=DATA_SHIFT_ID "
							+ "JOIN EXCEPTIONTYPES ON EXCP_ID=DATA_EXCEPTION_ID " + "JOIN USERDATA ON USER_ID=DATA_USER_ID "
							+ "WHERE CAL_MONTH= ? AND CAL_YEAR=? ORDER BY SHIFT_ID,USER_NAME",month,year);
			
			shift=mapper.mapShift(srs,Integer.valueOf(month),Integer.valueOf(year));
		}catch(Exception e) {
			e.printStackTrace();
		}
		return shift;
	}
	
	public Shift getShiftByMonthYearActual(String month, String year) {
		Shift shift=null;
		if (month == null || year == null) {
			java.util.Date date = new Date();
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			month = String.valueOf(cal.get(Calendar.MONTH) + 1);
			year = String.valueOf(cal.get(Calendar.YEAR));
		}
		SqlRowSet srs;
		try {
			srs = jdbcTemplate
					.queryForRowSet("SELECT DATA_ID,CAL_MONTH,CAL_YEAR,SHIFT_ID,SHIFT_NAME,SHIFT_START,SHIFT_END,SHIFT_TIMEZONE, "
							+ "USER_ID,USER_NAME,EXCP_ID,EXCP_EXCEPTIONNAME, " + "DATA_DATES " + "FROM SHIFTDATA_ACTUAL "
							+ "JOIN CALENDAR ON CAL_ID=DATA_CALENDAR_ID " + "JOIN SHIFTS ON SHIFT_ID=DATA_SHIFT_ID "
							+ "JOIN EXCEPTIONTYPES ON EXCP_ID=DATA_EXCEPTION_ID " + "JOIN USERDATA ON USER_ID=DATA_USER_ID "
							+ "WHERE CAL_MONTH= ? AND CAL_YEAR=? ORDER BY SHIFT_ID,USER_NAME",month,year);
			
			shift=mapper.mapShift(srs,Integer.valueOf(month),Integer.valueOf(year));
		}catch(Exception e) {
			e.printStackTrace();
		}
		return shift;
	}
	
	public Shift getShiftByMonthYearUser(String month, String year, String user) {
		Shift shift=null;
		if (month == null || year == null) {
			java.util.Date date = new Date();
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			month = String.valueOf(cal.get(Calendar.MONTH) + 1);
			year = String.valueOf(cal.get(Calendar.YEAR));
		}
		SqlRowSet srs;
		try {
			srs = jdbcTemplate
					.queryForRowSet("SELECT CAL_MONTH,CAL_YEAR,SHIFT_ID,SHIFT_NAME,SHIFT_START,SHIFT_END,SHIFT_TIMEZONE, " + 
							"USER_ID,USER_NAME,GROUP_CONCAT(EXCP_CODE||'~'||DATA_DATES_PROPOSED SEPARATOR '-') DATES,'P' TYPE_OF_DATA " + 
							"FROM SHIFTDATA " + 
							"JOIN CALENDAR ON CAL_ID=DATA_CALENDAR_ID " + 
							"JOIN SHIFTS ON SHIFT_ID=DATA_SHIFT_ID " + 
							"JOIN EXCEPTIONTYPES ON EXCP_ID=DATA_EXCEPTION_ID " + 
							"JOIN USERDATA ON USER_ID=DATA_USER_ID " + 
							"WHERE CAL_MONTH= ? " + 
							"AND CAL_YEAR=? AND USER_ID=?" + 
							"GROUP BY CAL_MONTH,CAL_YEAR,SHIFT_ID,SHIFT_NAME,SHIFT_START,SHIFT_END,SHIFT_TIMEZONE, " + 
							"USER_ID,USER_NAME",month,year,user);
			
			shift=mapper.mapShiftNew(srs,Integer.valueOf(month),Integer.valueOf(year));
		}catch(Exception e) {
			e.printStackTrace();
		}
		return shift;
	}
	
	public Shift getShiftByMonthYearExcel(String month, String year) {
		Shift shift=null;
		if (month == null || year == null) {
			java.util.Date date = new Date();
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			month = String.valueOf(cal.get(Calendar.MONTH) + 1);
			year = String.valueOf(cal.get(Calendar.YEAR));
		}
		SqlRowSet srs;
		try {
			srs = jdbcTemplate
					.queryForRowSet("SELECT CAL_MONTH,CAL_YEAR,SHIFT_ID,SHIFT_NAME,SHIFT_START,SHIFT_END,SHIFT_TIMEZONE,  " + 
							" USER_ID,USER_NAME,GROUP_CONCAT(EXCP_CODE||'~'||DATA_DATES SEPARATOR '-') DATES, 'P' TYPE_OF_DATA   " + 
							" FROM SHIFTDATA_PLANNED   " + 
							" JOIN CALENDAR ON CAL_ID=DATA_CALENDAR_ID   " + 
							" JOIN SHIFTS ON SHIFT_ID=DATA_SHIFT_ID   " + 
							" JOIN EXCEPTIONTYPES ON EXCP_ID=DATA_EXCEPTION_ID   " + 
							" JOIN USERDATA ON USER_ID=DATA_USER_ID   " + 
							" WHERE CAL_MONTH= ?  AND CAL_YEAR=?  " + 
							" GROUP BY CAL_MONTH,CAL_YEAR,SHIFT_ID,SHIFT_NAME,SHIFT_START,SHIFT_END,SHIFT_TIMEZONE,  USER_ID,USER_NAME "+
							"ORDER BY SHIFT_NAME",month,year);
			
			shift=mapper.mapShiftNew(srs,Integer.valueOf(month),Integer.valueOf(year));
		}catch(Exception e) {
			e.printStackTrace();
		}
		return shift;
	}
	
	public Shift getVarianceByMonthYear(String month, String year) {
		Shift shift=null;
		if (month == null || year == null) {
			java.util.Date date = new Date();
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			month = String.valueOf(cal.get(Calendar.MONTH) + 1);
			year = String.valueOf(cal.get(Calendar.YEAR));
		}
		SqlRowSet srs;
		try {
			srs = jdbcTemplate
					.queryForRowSet("SELECT CAL_MONTH,CAL_YEAR,SHIFT_ID,SHIFT_NAME,SHIFT_START,SHIFT_END,SHIFT_TIMEZONE, " + 
							"USER_ID,USER_NAME,GROUP_CONCAT(EXCP_CODE||'~'||DATA_DATES SEPARATOR '-') DATES,'A' TYPE_OF_DATA " + 
							"FROM SHIFTDATA_ACTUAL " + 
							"JOIN CALENDAR ON CAL_ID=DATA_CALENDAR_ID " + 
							"JOIN SHIFTS ON SHIFT_ID=DATA_SHIFT_ID " + 
							"JOIN EXCEPTIONTYPES ON EXCP_ID=DATA_EXCEPTION_ID " + 
							"JOIN USERDATA ON USER_ID=DATA_USER_ID " + 
							"WHERE CAL_MONTH= ? " + 
							"AND CAL_YEAR=? " + 
							"group by  " + 
							"CAL_MONTH,CAL_YEAR,SHIFT_ID,SHIFT_NAME,SHIFT_START,SHIFT_END,SHIFT_TIMEZONE,  " + 
							"USER_ID,USER_NAME,TYPE_OF_DATA " + 
							"UNION " + 
							"SELECT CAL_MONTH,CAL_YEAR,SHIFT_ID,SHIFT_NAME,SHIFT_START,SHIFT_END,SHIFT_TIMEZONE,  " + 
							"USER_ID,USER_NAME,GROUP_CONCAT(EXCP_CODE||'~'||DATA_DATES SEPARATOR '-') DATES,'P' TYPE_OF_DATA  " + 
							"FROM SHIFTDATA_PLANNED  " + 
							"JOIN CALENDAR ON CAL_ID=DATA_CALENDAR_ID  " + 
							"JOIN SHIFTS ON SHIFT_ID=DATA_SHIFT_ID  " + 
							"JOIN EXCEPTIONTYPES ON EXCP_ID=DATA_EXCEPTION_ID  " + 
							"JOIN USERDATA ON USER_ID=DATA_USER_ID  " + 
							"WHERE CAL_MONTH= ? " + 
							"AND CAL_YEAR=? " + 
							"group by  " + 
							"CAL_MONTH,CAL_YEAR,SHIFT_ID,SHIFT_NAME,SHIFT_START,SHIFT_END,SHIFT_TIMEZONE,  " + 
							"USER_ID,USER_NAME,TYPE_OF_DATA " + 
							"order by USER_NAME,TYPE_OF_DATA DESC",month,year,month,year);
			
			shift=mapper.mapShiftNew(srs,Integer.valueOf(month),Integer.valueOf(year));
		}catch(Exception e) {
			e.printStackTrace();
		}
		return shift;
	}
	
	public void shiftUpdate(Shift shift) {
		HashMap<String,Object> insertMap,updateMap ;
		List<Integer> usersUpdated = new ArrayList<Integer>();
		List<HashMap<String,Object>> insertList = new ArrayList<HashMap<String,Object>>();
		List<HashMap<String,Object>> updateList = new ArrayList<HashMap<String,Object>>();
		for(UserShiftData userShift:shift.getUsershift()) {
			if(usersUpdated.contains(userShift.getUser_id()))
				continue;
			usersUpdated.add(userShift.getUser_id());
			for(ExceptionData exception:userShift.getExceptionData()) {
				if(exception.getData_Id() == null) {
					//Insert a new record
					insertMap = new HashMap<String,Object>();
					insertMap.put("DATA_CALENDAR_ID", shift.getCalendar_id());
					insertMap.put("DATA_SHIFT_ID", userShift.getShift_id());
					insertMap.put("DATA_EXCEPTION_ID", exception.getExcp_id());
					insertMap.put("DATA_USER_ID", userShift.getUser_id());
					insertMap.put("DATA_DATES", Arrays.toString(exception.getDates()).replace("[", "").replace("]", ""));
					insertMap.put("DATA_LASTUPDATED", new java.sql.Timestamp(new java.util.Date().getTime()));
					insertMap.put("DATA_LASTUPDATEDBY", "ADMIN");
					insertList.add(insertMap);
				}else {
					//update the existing record
					updateMap = new HashMap<String,Object>();
					updateMap.put("DATA_CALENDAR_ID", shift.getCalendar_id());
					updateMap.put("DATA_SHIFT_ID", userShift.getShift_id());
					updateMap.put("DATA_EXCEPTION_ID", exception.getExcp_id());
					updateMap.put("DATA_USER_ID", userShift.getUser_id());
					updateMap.put("DATA_DATES", Arrays.toString(exception.getDates()).replace("[", "").replace("]", ""));
					updateMap.put("DATA_LASTUPDATED", new java.sql.Timestamp(new java.util.Date().getTime()));
					updateMap.put("DATA_LASTUPDATEDBY", "ADMIN");
					updateMap.put("DATA_ID",exception.getData_Id());
					updateList.add(updateMap);
				}
			}
		}
		if(insertList.size()>0)insert(insertList);
		if(updateList.size()>0)update(updateList);

    }
	
	public void shiftUpdateActual(Shift shift) {
		HashMap<String,Object> insertMap,updateMap ;
		List<Integer> usersUpdated = new ArrayList<Integer>();
		List<HashMap<String,Object>> insertList = new ArrayList<HashMap<String,Object>>();
		List<HashMap<String,Object>> updateList = new ArrayList<HashMap<String,Object>>();
		for(UserShiftData userShift:shift.getUsershift()) {
			if(usersUpdated.contains(userShift.getUser_id()))
				continue;
			usersUpdated.add(userShift.getUser_id());
			for(ExceptionData exception:userShift.getExceptionData()) {
				if(exception.getData_Id() == null) {
					//Insert a new record
					insertMap = new HashMap<String,Object>();
					insertMap.put("DATA_CALENDAR_ID", shift.getCalendar_id());
					insertMap.put("DATA_SHIFT_ID", userShift.getShift_id());
					insertMap.put("DATA_EXCEPTION_ID", exception.getExcp_id());
					insertMap.put("DATA_USER_ID", userShift.getUser_id());
					insertMap.put("DATA_DATES", Arrays.toString(exception.getDates()).replace("[", "").replace("]", ""));
					insertMap.put("DATA_LASTUPDATED", new java.sql.Timestamp(new java.util.Date().getTime()));
					insertMap.put("DATA_LASTUPDATEDBY", "ADMIN");
					insertList.add(insertMap);
				}else {
					//update the existing record
					updateMap = new HashMap<String,Object>();
					updateMap.put("DATA_CALENDAR_ID", shift.getCalendar_id());
					updateMap.put("DATA_SHIFT_ID", userShift.getShift_id());
					updateMap.put("DATA_EXCEPTION_ID", exception.getExcp_id());
					updateMap.put("DATA_USER_ID", userShift.getUser_id());
					updateMap.put("DATA_DATES", Arrays.toString(exception.getDates()).replace("[", "").replace("]", ""));
					updateMap.put("DATA_LASTUPDATED", new java.sql.Timestamp(new java.util.Date().getTime()));
					updateMap.put("DATA_LASTUPDATEDBY", "ADMIN");
					updateMap.put("DATA_ID",exception.getData_Id());
					updateList.add(updateMap);
				}
			}
		}
		if(insertList.size()>0)insertActual(insertList);
		if(updateList.size()>0)updateActual(updateList);

    }
	
	public void insert(List<HashMap<String,Object>> shiftData) {
		for (int i = 0; i < shiftData.size(); i += INSERT_BATCH_SIZE) {
			final List<HashMap<String,Object>> batchList = shiftData.subList(i, i
					+ INSERT_BATCH_SIZE > shiftData.size() ? shiftData.size() : i
					+ INSERT_BATCH_SIZE);
			jdbcTemplate.batchUpdate(insertQuery,
					new BatchPreparedStatementSetter() {
						@Override
						public void setValues(PreparedStatement pStmt, int j)
								throws SQLException {
							Map<String,Object> map = batchList.get(j);

							pStmt.setInt(1, Integer.parseInt(map.get("DATA_CALENDAR_ID").toString()));
							pStmt.setInt(2, Integer.parseInt(map.get("DATA_SHIFT_ID").toString()));
							pStmt.setInt(3, Integer.parseInt(map.get("DATA_EXCEPTION_ID").toString()));
							pStmt.setInt(4, Integer.parseInt(map.get("DATA_USER_ID").toString()));
							pStmt.setString(5, map.get("DATA_DATES").toString());
							pStmt.setTimestamp(6, (java.sql.Timestamp)map.get("DATA_LASTUPDATED"));
							pStmt.setString(7, (String)map.get("DATA_LASTUPDATEDBY"));
						}
						@Override
						public int getBatchSize() {
							return batchList.size();
						}
					});
			
			jdbcTemplate.batchUpdate(insertActualQuery,
					new BatchPreparedStatementSetter() {
						@Override
						public void setValues(PreparedStatement pStmt, int j)
								throws SQLException {
							Map<String,Object> map = batchList.get(j);

							pStmt.setInt(1, Integer.parseInt(map.get("DATA_CALENDAR_ID").toString()));
							pStmt.setInt(2, Integer.parseInt(map.get("DATA_SHIFT_ID").toString()));
							pStmt.setInt(3, Integer.parseInt(map.get("DATA_EXCEPTION_ID").toString()));
							pStmt.setInt(4, Integer.parseInt(map.get("DATA_USER_ID").toString()));
							pStmt.setString(5, map.get("DATA_DATES").toString());
							pStmt.setTimestamp(6, (java.sql.Timestamp)map.get("DATA_LASTUPDATED"));
							pStmt.setString(7, (String)map.get("DATA_LASTUPDATEDBY"));
						}
						@Override
						public int getBatchSize() {
							return batchList.size();
						}
					});
		}
	}
	
	public void insertActual(List<HashMap<String,Object>> shiftData) {
		for (int i = 0; i < shiftData.size(); i += INSERT_BATCH_SIZE) {
			final List<HashMap<String,Object>> batchList = shiftData.subList(i, i
					+ INSERT_BATCH_SIZE > shiftData.size() ? shiftData.size() : i
					+ INSERT_BATCH_SIZE);
			
			jdbcTemplate.batchUpdate(insertActualQuery,
					new BatchPreparedStatementSetter() {
						@Override
						public void setValues(PreparedStatement pStmt, int j)
								throws SQLException {
							Map<String,Object> map = batchList.get(j);

							pStmt.setInt(1, Integer.parseInt(map.get("DATA_CALENDAR_ID").toString()));
							pStmt.setInt(2, Integer.parseInt(map.get("DATA_SHIFT_ID").toString()));
							pStmt.setInt(3, Integer.parseInt(map.get("DATA_EXCEPTION_ID").toString()));
							pStmt.setInt(4, Integer.parseInt(map.get("DATA_USER_ID").toString()));
							pStmt.setString(5, map.get("DATA_DATES").toString());
							pStmt.setTimestamp(6, (java.sql.Timestamp)map.get("DATA_LASTUPDATED"));
							pStmt.setString(7, (String)map.get("DATA_LASTUPDATEDBY"));
						}
						@Override
						public int getBatchSize() {
							return batchList.size();
						}
					});
		}
	}
	
	public void update(List<HashMap<String,Object>> shiftData) {
		for (int i = 0; i < shiftData.size(); i += UPDATE_BATCH_SIZE) {
			final List<HashMap<String,Object>> batchList = shiftData.subList(i, i
					+ UPDATE_BATCH_SIZE > shiftData.size() ? shiftData.size() : i
					+ UPDATE_BATCH_SIZE);
			jdbcTemplate.batchUpdate(updateQuery,
					new BatchPreparedStatementSetter() {
						@Override
						public void setValues(PreparedStatement pStmt, int j)
								throws SQLException {
							Map<String,Object> map = batchList.get(j);

							pStmt.setInt(1, Integer.parseInt(map.get("DATA_CALENDAR_ID").toString()));
							pStmt.setInt(2, Integer.parseInt(map.get("DATA_SHIFT_ID").toString()));
							pStmt.setInt(3, Integer.parseInt(map.get("DATA_EXCEPTION_ID").toString()));
							pStmt.setInt(4, Integer.parseInt(map.get("DATA_USER_ID").toString()));
							pStmt.setString(5, map.get("DATA_DATES").toString());
							pStmt.setTimestamp(6, (java.sql.Timestamp)map.get("DATA_LASTUPDATED"));
							pStmt.setString(7, (String)map.get("DATA_LASTUPDATEDBY"));
							pStmt.setInt(8, Integer.parseInt(map.get("DATA_ID").toString()));
						}
						@Override
						public int getBatchSize() {
							return batchList.size();
						}
					});
			jdbcTemplate.batchUpdate(updateActualQuery,
					new BatchPreparedStatementSetter() {
						@Override
						public void setValues(PreparedStatement pStmt, int j)
								throws SQLException {
							Map<String,Object> map = batchList.get(j);

							pStmt.setInt(1, Integer.parseInt(map.get("DATA_CALENDAR_ID").toString()));
							pStmt.setInt(2, Integer.parseInt(map.get("DATA_SHIFT_ID").toString()));
							pStmt.setInt(3, Integer.parseInt(map.get("DATA_EXCEPTION_ID").toString()));
							pStmt.setInt(4, Integer.parseInt(map.get("DATA_USER_ID").toString()));
							pStmt.setString(5, map.get("DATA_DATES").toString());
							pStmt.setTimestamp(6, (java.sql.Timestamp)map.get("DATA_LASTUPDATED"));
							pStmt.setString(7, (String)map.get("DATA_LASTUPDATEDBY"));
							pStmt.setInt(8, Integer.parseInt(map.get("DATA_ID").toString()));
						}
						@Override
						public int getBatchSize() {
							return batchList.size();
						}
					});
		}
	}
	
	public void updateActual(List<HashMap<String,Object>> shiftData) {
		for (int i = 0; i < shiftData.size(); i += UPDATE_BATCH_SIZE) {
			final List<HashMap<String,Object>> batchList = shiftData.subList(i, i
					+ UPDATE_BATCH_SIZE > shiftData.size() ? shiftData.size() : i
					+ UPDATE_BATCH_SIZE);
			jdbcTemplate.batchUpdate(updateActualQuery,
					new BatchPreparedStatementSetter() {
						@Override
						public void setValues(PreparedStatement pStmt, int j)
								throws SQLException {
							Map<String,Object> map = batchList.get(j);

							pStmt.setInt(1, Integer.parseInt(map.get("DATA_CALENDAR_ID").toString()));
							pStmt.setInt(2, Integer.parseInt(map.get("DATA_SHIFT_ID").toString()));
							pStmt.setInt(3, Integer.parseInt(map.get("DATA_EXCEPTION_ID").toString()));
							pStmt.setInt(4, Integer.parseInt(map.get("DATA_USER_ID").toString()));
							pStmt.setString(5, map.get("DATA_DATES").toString());
							pStmt.setTimestamp(6, (java.sql.Timestamp)map.get("DATA_LASTUPDATED"));
							pStmt.setString(7, (String)map.get("DATA_LASTUPDATEDBY"));
							pStmt.setInt(8, Integer.parseInt(map.get("DATA_ID").toString()));
						}
						@Override
						public int getBatchSize() {
							return batchList.size();
						}
					});
		}
	}
	
	public List<Integer> getYears() {
		SqlRowSet srs = jdbcTemplate.queryForRowSet("SELECT YEAR(CURRENT_DATE)-1  AS CAL_YEAR FROM (VALUES(0)) " + 
				"UNION " + 
				"SELECT YEAR(CURRENT_DATE) AS CAL_YEAR FROM (VALUES(0)) " + 
				"UNION " + 
				"SELECT YEAR(CURRENT_DATE)+1  AS CAL_YEAR FROM (VALUES(0)) " + 
				"UNION " + 
				"SELECT DISTINCT CAL_YEAR FROM CALENDAR " + 
				"ORDER BY CAL_YEAR");
		List<Integer> years = new ArrayList<Integer>();
		int year = 0;
		while (srs.next()) {
			year = srs.getInt("CAL_YEAR");
			years.add(year);

		}
		if (!years.contains(Calendar.getInstance().get(Calendar.YEAR) - 1)) {
			years.add(Calendar.getInstance().get(Calendar.YEAR) - 1);
		}
		if (!years.contains(Calendar.getInstance().get(Calendar.YEAR) + 1)) {
			years.add(Calendar.getInstance().get(Calendar.YEAR) + 1);
		}
		if (!years.contains(Calendar.getInstance().get(Calendar.YEAR))) {
			years.add(Calendar.getInstance().get(Calendar.YEAR));
		}
		Collections.sort(years);
		return years;
	}



	public int insertCalendarItem(Map<String, Object> inputMap) {

		SimpleJdbcInsert insertDao = new SimpleJdbcInsert(jdbcTemplate);
		insertDao.setTableName("CALENDAR");
		insertDao.setGeneratedKeyName("CAL_ID");
		int id = insertDao.executeAndReturnKey(inputMap).intValue();

		return id;
	}
	
	@Cacheable
	public ArrayList<Integer> getExistingEmpId() {
		System.out.println(System.currentTimeMillis());
		SqlRowSet srs = jdbcTemplate.queryForRowSet("SELECT DISTINCT USER_EMP_ID FROM USERDATA WHERE USER_IS_ACTIVE='Y' AND USER_EMP_ID IS NOT NULL");
		ArrayList<Integer> list = new ArrayList<Integer>();
		while(srs.next()) {
			list.add(srs.getInt("USER_EMP_ID"));
		}
		System.out.println(System.currentTimeMillis());
		return list;
	}
	
	public int getCalendarId(int month, int year) {
		SqlRowSet srs = jdbcTemplate.queryForRowSet(getCalIdQuery, month,year);
		int cal_id=-1;
		while(srs.next()) {
			cal_id = srs.getInt("CAL_ID");
		}
		return cal_id;

	}

	public void setCalendarId(Shift shift) {
		shift.setCalendar_id(getCalendarId(shift.getMonth_id(),shift.getYear()));

	}

	public List<DayProp> getUserShiftForMonthYear(String month, String year,String userId) {
		List<DayProp> userShift=null;
		if (month == null || year == null) {
			java.util.Date date = new Date();
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			month = String.valueOf(cal.get(Calendar.MONTH) + 1);
			year = String.valueOf(cal.get(Calendar.YEAR));
		}
		SqlRowSet srs;
		try {
			srs = jdbcTemplate
					.queryForRowSet("select cal_month,cal_year,SHIFT_ID,shift_name,shift_start,shift_end,shift_timezone, " + 
							"user_id,user_name,EXCP_EXCEPTIONNAME, " + 
							"DATA_DATES_PROPOSED " + 
							"from SHIFTDATA " + 
							"join calendar on cal_id=DATA_CALENDAR_ID " + 
							"join shifts on shift_id=DATA_SHIFT_ID " + 
							"join exceptiontypes on excp_id=DATA_EXCEPTION_ID " + 
							"join userdata on user_id=DATA_USER_ID " + 
							"where cal_month= 1 " + 
							"and cal_year=2020 " + 
							"and USER_ID=0",month,year,userId);
			
			userShift=mapper.mapUserShift(srs);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return userShift;
	}

	public List<AttendanceVariance> getAttendanceVariance(String month, String year) {
		List<AttendanceVariance> shift=null;
		if (month == null || year == null) {
			java.util.Date date = new Date();
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			month = String.valueOf(cal.get(Calendar.MONTH) + 1);
			year = String.valueOf(cal.get(Calendar.YEAR));
		}
		SqlRowSet srs;
		try {
			srs = jdbcTemplate
					.queryForRowSet("select distinct att_emp_id,date,att_punchtime,user_name ,att_date,user_id,att_calendar_id,cal_id,cal_month, cal_year " + 
							"from  userdata  " + 
							"join (select att_emp_id,att_date date,att_punchtime,att_calendar_id,CONVERT(extract (DAY from ATT_DATE),SQL_VARCHAR) att_date from attendance) on user_emp_id=att_emp_id " + 
							"join calendar on cal_id=att_calendar_id " + 
							"left join (select distinct data_user_id,CONVERT((a.dates),SQL_VARCHAR) act_date,data_calendar_id,cal_month,cal_year  " + 
							"from  " + 
							"shiftdata_actual " + 
							", calendar " + 
							", unnest(REGEXP_SUBSTRING_ARRAY(data_dates, '\\d+')) a(dates)  " + 
							"where cal_id=data_calendar_id " + 
							") data on data.data_user_id=user_id and att_date=act_date  " + 
							"where act_date is null  " + 
							"and cal_month=? and cal_year=?  " + 
							"and att_punchtime < (select stat_value from static_data where stat_name='SHIFT_MIN_THRESHOLD')  " + 
							"order by att_emp_id,date ",month,year);
			
			shift=mapper.mapTimeDeficit(srs);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return shift;
	}

	public void updateAttendance(List<Attendance> mapAttendanceSheet) throws Exception {
		int cal_id = getCalendarId(Integer.parseInt(mapAttendanceSheet.get(0).getDate().split("-")[1]), 
				Integer.parseInt(mapAttendanceSheet.get(0).getDate().split("-")[0]));
		ArrayList<Integer> existingEmpId = getExistingEmpId();
		System.out.println(existingEmpId.size());
		
		for (int i = 0; i < mapAttendanceSheet.size(); i += UPDATE_BATCH_SIZE) {
			final List<Attendance> batchList = mapAttendanceSheet.subList(i, i
					+ UPDATE_BATCH_SIZE > mapAttendanceSheet.size() ? mapAttendanceSheet.size() : i
					+ UPDATE_BATCH_SIZE);
			jdbcTemplate.batchUpdate(attMergeQuery,
					new BatchPreparedStatementSetter() {
						@Override
						public void setValues(PreparedStatement pStmt, int j)
								throws SQLException {
							Attendance att = batchList.get(j);

							pStmt.setString(1, att.getDate());
							pStmt.setInt(2, att.getEmp_id());
							pStmt.setDouble(3, att.getPunch_time());
							pStmt.setTimestamp(4,Timestamp.valueOf(LocalDateTime.now()));
							pStmt.setString(5, "ADMIN");
							pStmt.setInt(6, cal_id);
						}
						@Override
						public int getBatchSize() {
							return batchList.size();
						}
					});
		}
		
		
	}

}
