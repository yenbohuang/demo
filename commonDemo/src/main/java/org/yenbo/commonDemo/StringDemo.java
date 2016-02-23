package org.yenbo.commonDemo;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StringDemo {

	private static final Logger log = LoggerFactory.getLogger(StringDemo.class);
	
	public static void main(String[] args) {
		
		byteConversion();
	}
	
	public static void byteConversion() {
		
		String str = "Test string";
		byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
		
		log.info(Arrays.toString(bytes));
		log.info(new String(bytes, StandardCharsets.UTF_8));
	}
}
