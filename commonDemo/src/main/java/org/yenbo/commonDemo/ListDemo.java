package org.yenbo.commonDemo;

import java.util.ArrayList;
import java.util.Arrays;

public class ListDemo {

	public static void main(String[] args) {
		
		printArray();
		arrayToArrayList();
	}
	
	public static void printArray() {
		
		String str = "Test string";
		byte[] bytes = str.getBytes();
		
		System.out.println(Arrays.toString(bytes));
	}
	
	public static void arrayToArrayList() {
		
		String[] list = new String[2];
		list[0] = "string 1";
		list[1] = "string 2";
		
		System.out.println(new ArrayList<String>(Arrays.asList(list)));
	}
}
