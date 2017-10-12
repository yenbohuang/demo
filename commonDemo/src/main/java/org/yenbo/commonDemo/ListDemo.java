package org.yenbo.commonDemo;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ListDemo {

	private static final Logger log = LoggerFactory.getLogger(ListDemo.class);
	
	public static void main(String[] args) {
		
		printArray();
		arrayToArrayList();
	}
	
	public static void printArray() {
		
		String str = "Test string";
		byte[] bytes = str.getBytes();
		
		log.info(Arrays.toString(bytes));
	}
	
	public static void arrayToArrayList() {
		
		String[] list = new String[2];
		list[0] = "string 1";
		list[1] = "string 2";
		
		log.info(Arrays.asList(list).toString());
		log.info(Arrays.<String>asList("string A", "string B").toString());
	}
}
