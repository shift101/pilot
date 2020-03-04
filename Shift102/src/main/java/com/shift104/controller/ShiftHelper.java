package com.shift104.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;

import com.shift104.model.Shift;

public class ShiftHelper {

	public static Resource generateExcel(Shift shift) {
		ByteArrayResource excel=null;
		try {
			excel =  new ByteArrayResource(Files.readAllBytes(Paths.get("C:\\Users\\hsharma\\Documents\\Sonata_Support_Shift_Mar_2020.xlsx")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return excel;
	}

}
