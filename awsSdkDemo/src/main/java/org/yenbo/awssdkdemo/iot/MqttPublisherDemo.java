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
import java.time.ZonedDateTime;

import javax.net.SocketFactory;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yenbo.awssdkdemo.PropertyReader;

public class MqttPublisherDemo {

	private static final Logger log = LoggerFactory.getLogger(MqttPublisherDemo.class);
	
	public static void main(String[] args) {
		
		readProperties();
		
		try {
			MqttClient client = getMqttClient();
			client.connect(getMqttConnectOptions());
			
			MqttMessage message = new MqttMessage(ZonedDateTime.now().toString().getBytes());
			message.setQos(1);
			client.publish("topic/test", message);
		
			client.disconnect();
			
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		}
	}
	
	private static void readProperties() {
		PropertyReader.getInstance().getParam("iot.endpointAddress");
		PropertyReader.getInstance().getParam("iot.certificateFilePath");
		PropertyReader.getInstance().getParam("iot.thingName");
		PropertyReader.getInstance().getParam("iot.privateKeyFilePath");
	}
	
	private static String getAwsEndpoint() {
		
		String endpoint = String.format("ssl://%s:8883",
				PropertyReader.getInstance().getParam("iot.endpointAddress"));
		log.info(endpoint);
		return endpoint;
	}
	
	private static SocketFactory getSocketFactory() throws Exception {
		
		Certificate certificate = null;
		
		try (FileInputStream fileInputStream = new FileInputStream(
				PropertyReader.getInstance().getParam("iot.certificateFilePath"))) {
			
			CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
			certificate = certificateFactory.generateCertificate(fileInputStream);
		}
		
		// Run the following command and convert PEM file first.
		// openssl pkcs8 -topk8 -inform PEM -outform DER -in privateKey.pem  -nocrypt > pkcs8_key
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		
		PrivateKey privateKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(
				Files.readAllBytes(Paths.get(
						PropertyReader.getInstance().getParam("iot.privateKeyFilePath")))));
		
		KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
		keyStore.load(null, null);
		keyStore.setCertificateEntry("certificate", certificate);
		keyStore.setKeyEntry("private-key", privateKey, "".toCharArray(), new Certificate[] {certificate});
		
		SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
		
		KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(
				KeyManagerFactory.getDefaultAlgorithm());
		keyManagerFactory.init(keyStore, "".toCharArray());
		
		sslContext.init(keyManagerFactory.getKeyManagers(), null, null);
		
		return sslContext.getSocketFactory();
	}
	
	private static MqttClient getMqttClient() throws Exception {
		
		MemoryPersistence persistence = new MemoryPersistence();
		
		return new MqttClient(getAwsEndpoint(),
				PropertyReader.getInstance().getParam("iot.thingName"),
				persistence);
	}
	
	private static MqttConnectOptions getMqttConnectOptions() throws Exception {
		
		MqttConnectOptions connectOptions = new MqttConnectOptions();
		connectOptions.setCleanSession(true);
		connectOptions.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1_1);
		connectOptions.setSocketFactory(getSocketFactory());
		
		return connectOptions;
	}
}
