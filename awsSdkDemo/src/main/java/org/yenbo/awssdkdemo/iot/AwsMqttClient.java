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

import javax.net.SocketFactory;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yenbo.awssdkdemo.PropertyReader;

public class AwsMqttClient {

	private static final Logger log = LoggerFactory.getLogger(AwsMqttClient.class);
	
	private String endpoint;
	private Certificate certificate;
	private PrivateKey privateKey;
	private MqttClient client;
	private MqttConnectOptions connectOptions;
	
	public AwsMqttClient() throws Exception {
		
		// end point
		endpoint = String.format("ssl://%s:8883",
				PropertyReader.getInstance().getParam("iot.endpointAddress"));
		log.info(endpoint);
		
		// read files
		readCertificate();
		readPrivateKey();
		
		// create client
		MemoryPersistence persistence = new MemoryPersistence();
		
		client = new MqttClient(endpoint,
				PropertyReader.getInstance().getParam("iot.thingName"),
				persistence);
		
		// create connect options
		connectOptions = new MqttConnectOptions();
		connectOptions.setCleanSession(true);
		connectOptions.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1_1);
		connectOptions.setSocketFactory(getSocketFactory());
	}
	
	private void readCertificate() throws Exception {
		
		try (FileInputStream fileInputStream = new FileInputStream(
				PropertyReader.getInstance().getParam("iot.certificateFilePath"))) {
			
			CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
			certificate = certificateFactory.generateCertificate(fileInputStream);
		}
	}
	
	private void readPrivateKey() throws Exception {
		
		// TODO How do we do this without conversion by openssl command?
		
		// Run the following command and convert PEM file first.
		// openssl pkcs8 -topk8 -inform PEM -outform DER -in privateKey.pem  -nocrypt > pkcs8_key
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		
		privateKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(
				Files.readAllBytes(Paths.get(
						PropertyReader.getInstance().getParam("iot.privateKeyFilePath")))));
	}
	
	private SocketFactory getSocketFactory() throws Exception {
				
		KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
		keyStore.load(null, null);
		keyStore.setCertificateEntry("certificate", certificate);
		keyStore.setKeyEntry("private-key", privateKey, "".toCharArray(),
				new Certificate[] {certificate});

		KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(
				KeyManagerFactory.getDefaultAlgorithm());
		keyManagerFactory.init(keyStore, "".toCharArray());
		
		SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
		sslContext.init(keyManagerFactory.getKeyManagers(), null, null);
		
		return sslContext.getSocketFactory();
	}

	public void connect() throws MqttSecurityException, MqttException {
		client.connect(connectOptions);
	}
	
	public void disconnect() throws MqttException {
		client.disconnect();
	}
	
	public void publish(String topic, String message) throws MqttPersistenceException, MqttException {
		
		if (StringUtils.isBlank(topic)) {
			throw new IllegalArgumentException("topic is blank");
		}
		
		if (StringUtils.isBlank(message)) {
			throw new IllegalArgumentException("message is blank");
		}
		
		MqttMessage testMessage = new MqttMessage(message.getBytes());
		testMessage.setQos(1);
		client.publish(topic, testMessage);
	}
	
	public String getTopicForShadowUpdate() {
		
		return String.format("$aws/things/%s/shadow/update",
				PropertyReader.getInstance().getParam("iot.thingName"));
	}
}
