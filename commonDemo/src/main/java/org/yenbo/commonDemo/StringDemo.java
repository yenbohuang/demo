package org.yenbo.commonDemo;

import java.nio.charset.StandardCharsets;

public class StringDemo {

	public static void main(String[] args) {
		
		byteConversion();
	}
	
	public static void byteConversion() {
		
		String str = "Test string";
		byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
		
		System.out.println(bytes);
		System.out.println(new String(bytes, StandardCharsets.UTF_8));
	}
}
