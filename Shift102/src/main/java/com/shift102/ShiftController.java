package com.shift102;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShiftController {

	@Autowired
    private ShiftRepository shifts;

		   @RequestMapping("/shift")
		    public Shift getShifts() 
		    {
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
		      
		      shift = shifts.getShiftByMonthYear("12","2019");
		      return shift;
		    }
}
