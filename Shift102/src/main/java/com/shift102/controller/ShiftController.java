package com.shift102.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.shift102.model.Shift;
import com.shift102.repository.ShiftRepository;

@RestController
public class ShiftController {

	@Autowired
    private ShiftRepository shifts;

		   @GetMapping("/shift")
		    public Shift getShifts() 
		    {
			   /*
			  int[] weekoff= {2,3,9,10,16,17,23,24,30,31};
			  int[] leave= {4,19};
			  int[] unplanned= {14,29};
		      UserShift userShift = new UserShift(1, 1, "Himanshu Sharma", "General", "0800-1630",weekoff , leave,unplanned) ;
		      
		      int[] weekoffD= {4,5,11,12,18,19,25,26};
		      int[] leaveD= {4,19};
		      int[] unplannedD= {14,29};
		      UserShift userShiftD = new UserShift(2, 2, "Deepak Singh", "UK", "1430-2230",weekoffD , leaveD,unplannedD) ;
		      
		      List<UserShift> list = new ArrayList<UserShift>();
		      list.add(userShift);list.add(userShiftD);
		      Shift shift = new Shift(12, "December", 2019, list);
		      */
			   Shift shift = shifts.getShiftByMonthYear();
		      return shift;
		    }
		   
		   @GetMapping("/shift/{month}/{year}") 
		    public Shift getShiftsByMonthYear(@PathVariable int month,@PathVariable int year) 
		    {
			   Shift shift = shifts.getShiftByMonthYear(String.valueOf(month),String.valueOf(year));
		      return shift;
		    }
}
