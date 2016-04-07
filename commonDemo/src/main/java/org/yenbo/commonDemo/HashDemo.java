package org.yenbo.commonDemo;

import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.CRC32;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HashDemo {

	private static final Logger log = LoggerFactory.getLogger(HashDemo.class);
	
	public static void main(String[] args) {
		
		try {
			//byte[] binary = "".getBytes("UTF-8");
			byte[] binary = RandomUtils.nextBytes(20);
			byte[] key = "secret".getBytes();
			
			log.info("SHA-256=" + Hex.encodeHexString(MessageDigest.getInstance("SHA-256").digest(binary)));
			log.info("CRC32=" + getCrc32(binary));
			log.info("HMAC SHA-1: " + getHmacSha1(binary, key));
			
			md5(binary);
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		}
	}
	
	private static void md5(byte[] binary) throws NoSuchAlgorithmException, CloneNotSupportedException {
	    
	    /* the following source codes returned the same results */
	    
	    // digest directly
	    log.info("MD5 digest=" + Hex.encodeHexString(MessageDigest.getInstance("MD5").digest(binary)));
	    
	    // update, and digest
	    MessageDigest digest1 = MessageDigest.getInstance("MD5");
	    digest1.update(binary);
	    log.info("MD5 update/digest=" + Hex.encodeHexString(digest1.digest()));
	    
	    // reset, and digest
	    MessageDigest digest2 = MessageDigest.getInstance("MD5");
	    digest2.reset();
	    log.info("MD5 reset/digest=" + Hex.encodeHexString(digest2.digest(binary)));
	    
	    // reset, update, and digest
	    MessageDigest digest3 = MessageDigest.getInstance("MD5");
	    digest3.reset();
	    digest3.update(binary);
            log.info("MD5 reset/update/digest=" + Hex.encodeHexString(digest3.digest()));
            
            // clone, and digest
            MessageDigest digest4 = (MessageDigest) digest1.clone();
            log.info("MD5 clone/digest=" + Hex.encodeHexString(digest4.digest(binary)));
            
            /* return different values*/
            MessageDigest digest5 = (MessageDigest) digest1.clone();
            log.info("MD5 newValue/clone/digest=" + Hex.encodeHexString(digest5.digest(RandomUtils.nextBytes(20))));
            
            digest5.reset();
            log.info("MD5 newValue/reset/digest=" + Hex.encodeHexString(digest5.digest(RandomUtils.nextBytes(20))));
	}
	
	private static long getCrc32(byte[] input) {
		
		CRC32 crc32 = new CRC32();
		crc32.update(input);
		return crc32.getValue();
	}
	
	private static byte[] getHmacSha1(byte[] input, byte[] key)
			throws NoSuchAlgorithmException, InvalidKeyException {
		
		Mac mac = Mac.getInstance("HmacSHA1");
		mac.init(new SecretKeySpec(key, "HmacSHA1"));
		return mac.doFinal(input);
	}
}
