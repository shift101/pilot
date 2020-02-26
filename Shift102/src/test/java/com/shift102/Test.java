package com.shift102;

import java.sql.Array;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class Test {

	public static void main(String[] args) {
		java.util.Date date= new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		System.out.println(String.valueOf(cal.get(Calendar.MONTH)));
		System.out.println(String.valueOf(cal.get(Calendar.YEAR)));
		
		int[] list= {1,2,3,4,5,6};
		System.out.println(Arrays.toString(list).replace("[", "").replace("]", ""));
	}
}
