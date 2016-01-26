package org.yenbo.commonDemo.security;

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

import org.apache.commons.lang3.StringUtils;

public class KeyReader {

	public static final char[] KEY_STORE_PASSWORD = "".toCharArray();
	
	private String certificateFilePath;
	private String privateKeyFilePath;
	private Certificate certificate;
	private PrivateKey privateKey;
	private KeyStore keyStore;
	private SSLContext sslContext;
	
	public KeyReader(String certificateFilePath, String privateKeyFilePath) throws Exception {
		
		if (StringUtils.isBlank(certificateFilePath)) {
			throw new IllegalArgumentException("certificateFilePath is blank");
		}
		
		if (StringUtils.isBlank(privateKeyFilePath)) {
			throw new IllegalArgumentException("privateKeyFilePath is blank");
		}
		
		this.certificateFilePath = certificateFilePath;
		this.privateKeyFilePath = privateKeyFilePath;
		
		// Certificate
		try (FileInputStream fileInputStream = new FileInputStream(certificateFilePath)) {
			CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
			certificate = certificateFactory.generateCertificate(fileInputStream);
		}

		// PrivateKey
		// JDK doesn't provide a means to load PEM key encoded in PKCS#1 without adding the
		// Bouncy Castle to the classpath. The JDK can only load PEM key encoded in PKCS#8 encoding.
		// Run the following command and convert PEM file first:
		// openssl pkcs8 -topk8 -inform PEM -outform DER -in privateKey.pem  -nocrypt > pkcs8_key
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		
		privateKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(Files.readAllBytes(
				Paths.get(privateKeyFilePath))));

		// KeyStore
		keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
		keyStore.load(null, null);
		keyStore.setCertificateEntry("certificate", certificate);
		keyStore.setKeyEntry("private-key", privateKey, KEY_STORE_PASSWORD,
				new Certificate[] {certificate});

		// SSLContext
		KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(
				KeyManagerFactory.getDefaultAlgorithm());
		keyManagerFactory.init(keyStore, KEY_STORE_PASSWORD);
		
		sslContext = SSLContext.getInstance("TLSv1.2");
		sslContext.init(keyManagerFactory.getKeyManagers(), null, null);
	}
	
	public SSLContext getSslContext() {
		return sslContext;
	}

	public String getCertificateFilePath() {
		return certificateFilePath;
	}

	public String getPrivateKeyFilePath() {
		return privateKeyFilePath;
	}

	public Certificate getCertificate() {
		return certificate;
	}

	public PrivateKey getPrivateKey() {
		return privateKey;
	}

	public KeyStore getKeyStore() {
		return keyStore;
	}
}
