package org.yenbo.commonDemo;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashDemo {

	public static byte[] getSha256Hash(byte[] input)
	throws NoSuchAlgorithmException, UnsupportedEncodingException {
		
	       MessageDigest digest = MessageDigest.getInstance("SHA-256");
	       digest.reset();
	       return digest.digest(input);
	 }
}
