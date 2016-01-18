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
import java.util.ArrayList;
import java.util.Arrays;

import javax.net.SocketFactory;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
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

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class AwsMqttClient {

	private static final Logger log = LoggerFactory.getLogger(AwsMqttClient.class);
	
	private String endpoint;
	private Certificate certificate;
	private PrivateKey privateKey;
	private MqttClient client;
	private MqttConnectOptions connectOptions;
	private Gson gson = new Gson();
	
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
		
		// callback
		client.setCallback(new MqttCallback() {
			
			@Override
			public void messageArrived(String arg0, MqttMessage arg1) throws Exception {

				log.info("Message arrived. topic: {}, QoS: {}, payload: {}", arg0, arg1.getQos(),
						new String(arg1.getPayload()));
			}
			
			@Override
			public void deliveryComplete(IMqttDeliveryToken arg0) {

				log.debug("Deliver complete. topics: {}", Arrays.toString(arg0.getTopics()));
			}
			
			@Override
			public void connectionLost(Throwable arg0) {
				log.error(arg0.getMessage(), arg0);
			}
		});
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
	
	public void publish(String topic, String payload) throws MqttPersistenceException, MqttException {
		
		if (StringUtils.isBlank(topic)) {
			throw new IllegalArgumentException("topic is blank");
		}
		
		log.info("Publish: topic={}, payload={}", topic, payload);
		
		MqttMessage testMessage = (payload != null) ? new MqttMessage(payload.getBytes()) :
			new MqttMessage();
		testMessage.setQos(0);
		client.publish(topic, testMessage);
	}
	
	public String getShadowJson() {
		
		JsonObject desired = new JsonObject();
		desired.addProperty("time", ZonedDateTime.now().toString());
		
		JsonObject state = new JsonObject();
		state.add("desired", desired);
		
		JsonObject payload = new JsonObject();
		payload.add("state", state);
		
		return gson.toJson(payload);
	}
	
	public void subscribe(ArrayList<String> topicFilters) throws MqttException {
		
		if (topicFilters == null) {
			throw new IllegalArgumentException("topicFilters is null");
		}
		
		int[] qos = new int[topicFilters.size()];
		Arrays.fill(qos, 0);
		
		log.info("Subscribe: " + topicFilters);
		
		client.subscribe(topicFilters.toArray(new String[0]), qos);
	}
	
	public void subscribe(String topicFilter) throws MqttException {
		
		if (StringUtils.isBlank(topicFilter)) {
			throw new IllegalArgumentException("topicFilter is blank");
		}
		
		log.info("Subscribe: " + topicFilter);
		
		client.subscribe(topicFilter, 0);
	}
	
	private static String getTopicForShadow(String pattern) {
		return String.format("$aws/things/%s/shadow/" + pattern,
				PropertyReader.getInstance().getParam("iot.thingName"));
	}
	
	public static String getTopicForShadowUpdate() {		
		return getTopicForShadow("update");
	}
	
	public static String getTopicForShadowUpdateAccepted() {
		return getTopicForShadow("update/accepted");
	}
	
	public static String getTopicForShadowUpdateRejected() {
		return getTopicForShadow("update/rejected");
	}
	
	public static String getTopicForShadowUpdateDelta() {
		return getTopicForShadow("update/delta");
	}
	
	public static String getTopicForShadowGet() {
		return getTopicForShadow("get");
	}
	
	public static String getTopicForShadowGetAccepted() {
		return getTopicForShadow("get/accepted");
	}
	
	public static String getTopicForShadowGetRejected() {
		return getTopicForShadow("get/rejected");
	}
	
	public static String getTopicForShadowDelete() {
		return getTopicForShadow("delete");
	}
	
	public static String getTopicForShadowDeleteAccepted() {
		return getTopicForShadow("delete/accepted");
	}
	
	public static String getTopicForShadowDeleteRejected() {
		return getTopicForShadow("delete/rejected");
	}
}
