package com.shift105;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;

public class Test {

	public static void main(String[] args) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MONTH, 0);
		cal.set(Calendar.YEAR, 2020);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		/*
		 * System.out.println(maxDay);
		 * System.out.println("WO~4,5,11,12,18,19,25,26".indexOf("25"));
		 * 
		 * String stri = "WO~4,5,11,12,18,19,25,26-PL~3,6";
		 * 
		 * for (int i = 1; i <= maxDay; i++) { for (String str : stri.split("-")) {
		 * String[] sep = str.split("~"); System.out.println("Checking for Date:"+i);
		 * if(Arrays.binarySearch(toIntArray(sep[1],","), i) > -1) {
		 * System.out.println("exc:"+sep[0]+" date:"+i); break; } } }
		 */
		/*
		 * for (int j = 1; j <= maxDay; j++) { SimpleDateFormat simpleDateformat = new
		 * SimpleDateFormat("E"); // the day of the week abbreviated
		 * System.out.println(simpleDateformat.format(cal.getFirstDayOfWeek())); }
		 */
		SimpleDateFormat simpleDayformat = new SimpleDateFormat("E");
		SimpleDateFormat simpleDateformat = new SimpleDateFormat("DD-MMM-YYYY");
		int myMonth=cal.get(Calendar.MONTH);
		while (myMonth==cal.get(Calendar.MONTH)) {
			  System.out.print(simpleDateformat.format(cal.getTime())+" ");
			  System.out.println(simpleDayformat.format(cal.getTime()));
			  cal.add(Calendar.DAY_OF_MONTH, 1);
			}
		
	}

	public static int[] toIntArray(String input, String delimiter) {

		return Arrays.stream(input.split(delimiter)).mapToInt(Integer::parseInt).toArray();
	}
}
