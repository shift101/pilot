package com.shift101.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shift101.models.Shift;

@RestController
public class ShiftController {

		   @RequestMapping("/shift")
		    public List<Shift> getShifts() 
		    {
		      List<Shift> employeesList = new ArrayList<Shift>();
		      employeesList.add(new Shift(1,"Himanshu","Sharma","hsharma@bravurasolutions.com"));
		      return employeesList;
		    }
}
