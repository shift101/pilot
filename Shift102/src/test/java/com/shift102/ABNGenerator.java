package com.shift102;

import java.util.Arrays;
import java.util.Random;

public class ABNGenerator {

	private final static int[] WEIGHT = { 10, 1, 3, 5, 7, 9, 11, 13, 15, 17, 19 };

	public static void main(String[] args) {
		int[] ABN = new int[11];
		int count = 0, 
				requiredNos = 100;//number of ABNs to be generated

		while (true) {
			int i = 0;
			while (i < 11) {
				Random rand = new Random();
				int number = 0;
				while (true) {
					number = rand.nextInt(10);
					if (number != 0)
						break;
				}
				ABN[i] = number;
				i++;
			}
			if (verifyABN(ABN)) {
				System.out.println(Arrays.toString(ABN).replace("[", "").replace("]", "").replace(",", ""));
				count++;
			}
			if (count == requiredNos)
				break;
		}

	}

	private static boolean verifyABN(int[] aFN) {
		int sum = 0;
		for (int i = 0; i < aFN.length; i++) {
			if (i == 0) {
				sum += (aFN[i] - 1) * WEIGHT[i];
			} else {
				sum += aFN[i] * WEIGHT[i];
			}
		}
		return sum % 89 == 0 ? true : false;
	}

}
