package org.yenbo.commonDemo.test;

import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yenbo.commonDemo.HashDemo;

public class HashDemoTest {

	private static final Logger log = LoggerFactory.getLogger(HashDemoTest.class);
	
	public static void main(String[] args) {
		
		try {
			log.info(Hex.encodeHexString(HashDemo.getSha256Hash("".getBytes("UTF-8"))));
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		}
	}
}
