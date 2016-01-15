package org.yenbo.awssdkdemo.iot;

import java.time.ZonedDateTime;
import java.util.ArrayList;

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
			
			ArrayList<String> topicFilters = new ArrayList<>();
			topicFilters.add("topic/test");
			topicFilters.add(client.getTopicForShadowUpdateAccepted());
			
			client.subscribe(topicFilters);
			
			client.publish("topic/test", ZonedDateTime.now().toString());
			client.publish(client.getTopicForShadowUpdate(), ZonedDateTime.now().toString());
			
			// TODO subscribe does not work yet
			Thread.sleep(10000);
			
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
