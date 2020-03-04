package com.shift104.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectConverter {
	static ObjectMapper mapper = new ObjectMapper();
	
	public static void printObject(Object obj){
		try {
			String prettyStaff1 = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
			System.out.println(prettyStaff1);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
