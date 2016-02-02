package org.yenbo.commonDemo;

import java.io.UnsupportedEncodingException;
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
	
	private static String ALGORITHM_SHA256 = "SHA-256";
	private static String ALGORITHM_MD5 = "MD5";
	
	public static void main(String[] args) {
		
		try {
			//byte[] binary = "".getBytes("UTF-8");
			byte[] binary = RandomUtils.nextBytes(20);
			byte[] key = "secret".getBytes();
			
			log.info("SHA-256=" + Hex.encodeHexString(hash(ALGORITHM_SHA256,binary)));
			log.info("MD5=" + Hex.encodeHexString(hash(ALGORITHM_MD5, binary)));
			log.info("CRC32=" + getCrc32(binary));
			log.info("HMAC SHA-1: " + getHmacSha1(binary, key));
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		}
	}
	
	private static byte[] hash(String algorithm, byte[] input)
	throws NoSuchAlgorithmException, UnsupportedEncodingException {
		
	       MessageDigest digest = MessageDigest.getInstance(algorithm);
	       digest.reset();
	       return digest.digest(input);
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
