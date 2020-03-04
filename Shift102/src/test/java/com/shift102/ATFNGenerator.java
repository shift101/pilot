package com.shift102;

import java.util.Arrays;
import java.util.Random;

public class ATFNGenerator {

	private final static int[] WEIGHT = { 1, 4, 3, 7, 5, 8, 6, 9, 10 };

	public static void main(String[] args) {

		int[] AFN = new int[9];
		int count=0,
				requiredNos=10000;//number of ATFNs to be generated
		
		while (true) {
			int i = 0;
			while (i < 9) {
				Random rand = new Random();
				int number = 0;
				while (true) {
					number = rand.nextInt(10);
					if (number != 0)
						break;
				}
				AFN[i] = number;
				i++;
			}
			if (verifyAFN(AFN)) {
				System.out.println(Arrays.toString(AFN).replace("[", "").replace("]", "").replace(",", ""));
				count++;
			}			
			if(count==requiredNos)break;
		}
		
	}

	private static boolean verifyAFN(int[] aFN) {
		int sum = 0;
		for (int i = 0; i < aFN.length; i++) {
			sum += aFN[i] * WEIGHT[i];
		}
		return sum % 11 == 0 ? true : false;
	}

}