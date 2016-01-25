package org.yenbo.awssdkdemo.iot;

import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.spec.PKCS8EncodedKeySpec;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;

import org.yenbo.awssdkdemo.PropertyReader;

public class KeyReader {

	private static Certificate readCertificate() throws Exception {
		
		try (FileInputStream fileInputStream = new FileInputStream(
				PropertyReader.getInstance().getParam("iot.certificateFilePath"))) {
			
			CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
			return certificateFactory.generateCertificate(fileInputStream);
		}
	}
	
	private static PrivateKey readPrivateKey() throws Exception {
		
		// JDK doesn't provide a means to load PEM key encoded in PKCS#1 without adding the
		// Bouncy Castle to the classpath. The JDK can only load PEM key encoded in PKCS#8 encoding.
		// Run the following command and convert PEM file first:
		// openssl pkcs8 -topk8 -inform PEM -outform DER -in privateKey.pem  -nocrypt > pkcs8_key
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		
		return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(Files.readAllBytes(Paths.get(
				PropertyReader.getInstance().getParam("iot.privateKeyFilePath")))));
	}
	
	public static SSLContext getSslContext() throws Exception {
		
		Certificate certificate = readCertificate();
		
		KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
		keyStore.load(null, null);
		keyStore.setCertificateEntry("certificate", certificate);
		keyStore.setKeyEntry("private-key", readPrivateKey(), "".toCharArray(),
				new Certificate[] {certificate});

		KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(
				KeyManagerFactory.getDefaultAlgorithm());
		keyManagerFactory.init(keyStore, "".toCharArray());
		
		SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
		sslContext.init(keyManagerFactory.getKeyManagers(), null, null);
		
		return sslContext;
	}
}
