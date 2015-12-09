package org.yenbo.commonDemo.test;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yenbo.commonDemo.HashDemo;

public class HashDemoTest {

	private static final Logger log = LoggerFactory.getLogger(HashDemoTest.class);
	
	public static void main(String[] args) {
		
		try {
			//byte[] binary = "".getBytes("UTF-8");
			byte[] binary = RandomUtils.nextBytes(20);
			
			log.info("SHA-256=" + Hex.encodeHexString(HashDemo.hash(HashDemo.ALGORITHM_SHA256,
					binary)));
			log.info("MD5=" + Hex.encodeHexString(HashDemo.hash(HashDemo.ALGORITHM_MD5, binary)));
			log.info("CRC32=" + HashDemo.getCrc32(binary));
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		}
	}
}
