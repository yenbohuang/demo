package org.yenbo.commonDemo;

public class ParserDemo {

	public static void main(String[] args) {
		
		// integer to HEX
		System.out.println(String.format("%016X", 123456L));
		
		// HEX to integer
		System.out.println(Long.decode("0x1234567ABCDE"));
	}

}
