package org.yenbo.awssdkdemo.iot;

import java.time.ZonedDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yenbo.awssdkdemo.PropertyReader;

public class MqttPublisherDemo {

	private static final Logger log = LoggerFactory.getLogger(MqttPublisherDemo.class);
	
	public static void main(String[] args) {
		
		readProperties();
		
		try {
			AwsMqttClient client = new AwsMqttClient();
			client.connect();
			
			client.publish("topic/test", ZonedDateTime.now().toString());
			client.publish(client.getTopicForShadowUpdate(), ZonedDateTime.now().toString());
			
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
}
