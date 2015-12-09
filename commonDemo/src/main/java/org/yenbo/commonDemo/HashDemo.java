package org.yenbo.commonDemo;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.CRC32;

public class HashDemo {

	public static String ALGORITHM_SHA256 = "SHA-256";
	public static String ALGORITHM_MD5 = "MD5";
	
	public static byte[] hash(String algorithm, byte[] input)
	throws NoSuchAlgorithmException, UnsupportedEncodingException {
		
	       MessageDigest digest = MessageDigest.getInstance(algorithm);
	       digest.reset();
	       return digest.digest(input);
	}
	
	public static long getCrc32(byte[] input) {
		
		CRC32 crc32 = new CRC32();
		crc32.update(input);
		return crc32.getValue();
	}
}
