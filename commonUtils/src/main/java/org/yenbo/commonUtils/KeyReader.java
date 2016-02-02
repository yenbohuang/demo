package org.yenbo.commonUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.List;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.xml.bind.DatatypeConverter;

import org.apache.commons.lang3.StringUtils;

public class KeyReader {

	public enum PrivateKeyType {
		RSA,
		EC
	}
	
	public static final char[] KEY_STORE_PASSWORD = "".toCharArray();
	
	private String certificateFilePath;
	private String privateKeyFilePath;
	private Certificate certificate;
	private PrivateKey privateKey;
	private KeyStore keyStore;
	private SSLContext sslContext;
	
	public KeyReader(String certificateFilePath, String privateKeyFilePath,
			PrivateKeyType privateKeyType)
	throws Exception {
		
		if (StringUtils.isBlank(certificateFilePath)) {
			throw new IllegalArgumentException("certificateFilePath is blank");
		}
		
		if (StringUtils.isBlank(privateKeyFilePath)) {
			throw new IllegalArgumentException("privateKeyFilePath is blank");
		}
		
		this.certificateFilePath = certificateFilePath;
		this.privateKeyFilePath = privateKeyFilePath;
		
		// Certificate
		certificate = readCertificate(certificateFilePath);

		// PrivateKey
		privateKey = readPrivateKey(privateKeyFilePath, privateKeyType);

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
	
	public static Certificate readCertificate(String filepath)
			throws FileNotFoundException, IOException, CertificateException {
		
		if (StringUtils.isBlank(filepath)) {
			throw new IllegalArgumentException("filepath is blank");
		}
		
		Certificate cert = null;
		
		try (FileInputStream fileInputStream = new FileInputStream(filepath)) {
			CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
			cert = certificateFactory.generateCertificate(fileInputStream);
		}
		
		return cert;
	}
	
	public static PrivateKey readPrivateKey(String filepath, PrivateKeyType type)
			throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
		
		if (StringUtils.isBlank(filepath)) {
			throw new IllegalArgumentException("filepath is blank");
		}
		
		KeyFactory keyFactory = null;
		
		switch (type) {
		case RSA:
			// JDK doesn't provide a means to load PEM key encoded in PKCS#1 without adding the
			// Bouncy Castle to the classpath. The JDK can only load PEM key encoded in PKCS#8 encoding.
			// Run the following command and convert PEM file first:
			// openssl pkcs8 -topk8 -inform PEM -outform DER -in privateKey.pem  -nocrypt > pkcs8_key
			keyFactory = KeyFactory.getInstance("RSA");
			
			return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(Files.readAllBytes(
					Paths.get(filepath))));

		case EC:
			
			List<String> lines = Files.readAllLines(Paths.get(filepath));
			StringBuilder stringBuilder = new StringBuilder();
			
			for (String line: lines) {
				
				// end of file
				if (line.contains("-----END PRIVATE KEY-----") ||
						line.contains("-----END RSA PRIVATE KEY-----")) {
					break;
				}
				
				// add key content
				if (StringUtils.isNotBlank(line) &&
						false == line.contains("-----BEGIN PRIVATE KEY-----")) {
					stringBuilder.append(line + "\n");
				} 
			}
			
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(DatatypeConverter.parseBase64Binary(
					stringBuilder.toString()));
			keyFactory = KeyFactory.getInstance("EC");
			
			return keyFactory.generatePrivate(keySpec);
			
		default:
			throw new CommonUtilsException("Unknown private key type");
		}
	}
}
