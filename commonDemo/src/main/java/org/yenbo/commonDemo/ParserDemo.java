package org.yenbo.commonDemo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ParserDemo {

	private static final Logger log = LoggerFactory.getLogger(ParserDemo.class);
	
	public static void main(String[] args) {
		
		// integer to HEX
		log.info(String.format("%016X", 123456L));
		
		// HEX to integer
		log.info(Long.decode("0x1234567ABCDE").toString());
	}

}
