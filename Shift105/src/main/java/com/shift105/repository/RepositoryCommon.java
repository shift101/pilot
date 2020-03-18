package com.shift105.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.shift105.model.ShiftStat;
import com.shift105.model.UserStat;

@Repository
public class RepositoryCommon {
	
	@Autowired
	protected JdbcTemplate jdbcTemplate;
	
	public List<UserStat> getAllUsers() {
		SqlRowSet srs = jdbcTemplate.queryForRowSet("SELECT USER_ID,USER_NAME FROM USERDATA");
		List<UserStat> users = new ArrayList<UserStat>();
		UserStat user;
		while (srs.next()) {
			user = new UserStat();
			user.setUserId(Integer.toString(srs.getInt("USER_ID")));
			user.setUserName(srs.getString("USER_NAME"));

			users.add(user);
		}

		return users;
	}
	
	public List<ShiftStat> getAllShifts() {
		SqlRowSet srs = jdbcTemplate
				.queryForRowSet("SELECT SHIFT_ID,SHIFT_NAME,SHIFT_START,SHIFT_END,SHIFT_TIMEZONE FROM SHIFTS");
		List<ShiftStat> shifts = new ArrayList<ShiftStat>();
		ShiftStat shift;
		while (srs.next()) {
			shift = new ShiftStat();
			shift.setShift_id((srs.getInt("SHIFT_ID")));
			shift.setShift_name((srs.getString("SHIFT_NAME")));
			shift.setShift_time(((srs.getString("SHIFT_START").concat("-")
					.concat(srs.getString("SHIFT_END").concat(" ").concat(srs.getString("SHIFT_TIMEZONE"))))));

			shifts.add(shift);
		}

		return shifts;
	}
	
	public List<com.shift105.model.Exception> getAllExceptions() {
		SqlRowSet srs = jdbcTemplate
				.queryForRowSet("SELECT EXCP_ID,EXCP_CODE FROM EXCEPTIONTYPES");
		List<com.shift105.model.Exception> excp= new ArrayList<com.shift105.model.Exception>();
		while (srs.next()) {
			excp.add(new com.shift105.model.Exception(srs.getInt("EXCP_ID"),srs.getString("EXCP_CODE")));
		}

		return excp;
	}
	
	

}
