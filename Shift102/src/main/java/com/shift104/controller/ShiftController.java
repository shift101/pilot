package com.shift104.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.shift104.model.Shift;
import com.shift104.model.ShiftStat;
import com.shift104.model.UserStat;
import com.shift104.repository.ShiftRepository;
import com.shift104.util.ObjectConverter;

@RestController
public class ShiftController {

	@Autowired
    private ShiftRepository shifts;

		   @GetMapping("/shift")
		    public Shift getShifts() 
		    {
			  Shift shift = shifts.getShiftByMonthYear();
		      return shift;
		    }
		   
		   @GetMapping("/shift/{month}/{year}") 
		    public Shift getShiftsByMonthYear(@PathVariable int month,@PathVariable int year) 
		    {
			   Shift shift = shifts.getShiftByMonthYear(String.valueOf(month),String.valueOf(year));
		      return shift;
		    }
		   @PostMapping("/shift/update")
		    public String addShiftInformation(@RequestBody Shift shift)
		    {			   
			   shifts.setCalendarId(shift);
			   if(shift.getCalendar_id()==null) {
					Map<String,Object> inputMap = new HashMap<String,Object>();
					inputMap.put("CAL_MONTH", shift.getMonth_id());
					inputMap.put("CAL_YEAR", shift.getYear());
					inputMap.put("CAL_LASTUPDATED",new java.sql.Timestamp(new java.util.Date().getTime()) );
					inputMap.put("CAL_LASTUPDATEDBY", "ADMIN");
					shift.setCalendar_id(Integer.toString(shifts.insertCalendarItem(inputMap)));
			   }
			   ObjectConverter.printObject(shift);
			   shifts.shiftUpdate(shift);
		      return "success_post";
		    }
		   
		   @GetMapping("/static/years")
		    public List<Integer> getYears()
		    {
		      return shifts.getYears();
		    }
		   
		   @GetMapping("/static/users")
		    public List<UserStat> getAllUsers()
		    {
		      return shifts.getAllUsers();
		    }
		   
		   @GetMapping("/static/exceptions")
		    public List<com.shift104.model.Exception> getExceptions()
		    {
		      return shifts.getAllExceptions();
		    }
		   @GetMapping("/generate/{month}/{year}") 
		    public ResponseEntity<Resource> generateExcel(@PathVariable int month,@PathVariable int year) throws IOException 
		    {
			   Shift shift = shifts.getShiftByMonthYear(String.valueOf(month),String.valueOf(year));
			   Resource resource = ShiftHelper.generateExcel(shift);

		      return ResponseEntity.ok()
		    		  .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Shift_Rota_"+month+"_"+year+".xlsx")// add headers if any
		    	        .contentLength(resource.contentLength())
		    	        .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
		    	        .body(resource);
		    }
		   
		   @GetMapping("/static/shifts")
		    public List<ShiftStat> getAllShifts()
		    {
		      return shifts.getAllShifts();
		    }
}
