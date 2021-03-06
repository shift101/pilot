package com.shift105.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.shift105.model.AttendanceVariance;
import com.shift105.model.Shift;
import com.shift105.model.ShiftStat;
import com.shift105.model.UserStat;
import com.shift105.repository.ShiftRepository;

@RestController
public class ShiftController {

	@Autowired
	private ShiftRepository shifts;
	@Autowired
	private ShiftHelper helper;

	@GetMapping("/shift")
	public Shift getShifts() {
		Shift shift = shifts.getShiftByMonthYear();
		return shift;
	}

	/*
	 * @GetMapping("/availableMonths") public HashMap<Integer,String>
	 * getAvailableMonths() { HashMap<Integer,String> mon =
	 * shifts.getAvailableMonths(); return mon; }
	 */

	@GetMapping("/shift/{month}/{year}")
	public Shift getShiftsByMonthYear(@PathVariable int month, @PathVariable int year) {
		Shift shift = shifts.getShiftByMonthYear(String.valueOf(month), String.valueOf(year));
		return shift;
	}
	
	@GetMapping("/shift/calendar/{month}/{year}")
	public int getCalendarId(@PathVariable int month, @PathVariable int year) {
		return shifts.getCalendarId(month, year);
	}
	
	@GetMapping("/shift/actual/{month}/{year}")
	public Shift getShiftsByMonthYearActual(@PathVariable int month, @PathVariable int year) {
		Shift shift = shifts.getShiftByMonthYearActual(String.valueOf(month), String.valueOf(year));
		return shift;
	}

	@GetMapping("/shift/{month}/{year}/{user}")
	public Shift getShiftsByMonthYearUser(@PathVariable int month, @PathVariable int year, @PathVariable int user) {
		Shift shift = shifts.getShiftByMonthYearUser(String.valueOf(month), String.valueOf(year), String.valueOf(user));
		return shift;
	}
	
	@GetMapping("/shift/variance/{month}/{year}")
	public Shift getShiftsByVariance(@PathVariable int month, @PathVariable int year) {
		Shift shift = shifts.getVarianceByMonthYear(String.valueOf(month), String.valueOf(year));
		return shift;
	}

	@PostMapping("/shift/update")
	public String addShiftPlanned(@RequestBody Shift shift) {
		/**
		 * this method will insert/update both the planned and actuals with same values.
		 */
		shifts.setCalendarId(shift);
		if (shift.getCalendar_id() == 0) {
			Map<String, Object> inputMap = new HashMap<String, Object>();
			inputMap.put("CAL_MONTH", shift.getMonth_id());
			inputMap.put("CAL_YEAR", shift.getYear());
			inputMap.put("CAL_LASTUPDATED", new java.sql.Timestamp(new java.util.Date().getTime()));
			inputMap.put("CAL_LASTUPDATEDBY", "ADMIN");
			shift.setCalendar_id(shifts.insertCalendarItem(inputMap));
		}
		//ObjectConverter.printObject(shift);
		shifts.shiftUpdate(shift);
		return "success_post";
	}
	
	@PostMapping("/shift/update/actual")
	public String addShiftActual(@RequestBody Shift shift) {
		shifts.setCalendarId(shift);
		//ObjectConverter.printObject(shift);
		shifts.shiftUpdateActual(shift);
		return "success_post";
	}

	@GetMapping("/static/years")
	public List<Integer> getYears() {
		return shifts.getYears();
	}

	@GetMapping("/static/users")
	public List<UserStat> getAllUsers() {
		return shifts.getAllUsers();
	}

	@GetMapping("/static/exceptions")
	public List<com.shift105.model.Exception> getExceptions() {
		return shifts.getAllExceptions();
	}

	@GetMapping("/generate/{month}/{year}")
	public ResponseEntity<byte[]> generateExcel(@PathVariable int month, @PathVariable int year) throws IOException {
		Shift shift = shifts.getShiftByMonthYearExcel(String.valueOf(month), String.valueOf(year));
		byte[] out=null;
		try {
			out = helper.generateExcel(shift);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("file length:"+out.length);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION,
						"attachment; filename=Shift_Rota_" + month + "_" + year + ".xlsx")// add headers if any
				.contentLength(out.length)
				.contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
				.body(out);
	}

	@GetMapping("/static/shifts")
	public List<ShiftStat> getAllShifts() {
		return shifts.getAllShifts();
	}
	
	@GetMapping("/shift/att_variance/{month}/{year}")
	public List<AttendanceVariance> getAttendanceVariance(@PathVariable String month, @PathVariable String year) {
		return shifts.getAttendanceVariance(month, year);
	}
	
}
