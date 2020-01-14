package com.shift102;

import java.util.Calendar;
import java.util.Date;

public class Test {

	public static void main(String[] args) {
		java.util.Date date= new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		System.out.println(String.valueOf(cal.get(Calendar.MONTH)));
		System.out.println(String.valueOf(cal.get(Calendar.YEAR)));
	}
}
