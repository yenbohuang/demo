package org.yenbo.awssdkdemo.iot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.iot.AWSIotClient;
import com.amazonaws.services.iot.model.CreateThingRequest;
import com.amazonaws.services.iot.model.CreateThingResult;
import com.amazonaws.services.iot.model.DescribeThingRequest;
import com.amazonaws.services.iot.model.DescribeThingResult;

public class AwsIotDemoConsole {

	private static final Logger log = LoggerFactory.getLogger(AwsIotDemoConsole.class);
	
	private static AWSIotClient client = new AWSIotClient();
	
	public static void main(String[] args) {
		
		describeThing();
	}
	
	public static CreateThingResult createThing() {
		
		CreateThingRequest createThingRequest = new CreateThingRequest();
		createThingRequest.setThingName(AwsDemoConstants.IOT_THING_NAME);
		
		CreateThingResult createThingResult = client.createThing(createThingRequest);
		
		log.info("ARN=" + createThingResult.getThingArn());
		log.info("ThingName=", createThingResult.getThingName());
		
		return createThingResult;
	}
	
	public static void describeThing() {
		
		DescribeThingRequest describeThingRequest = new DescribeThingRequest();
		describeThingRequest.setThingName(AwsDemoConstants.IOT_THING_NAME);
		
		DescribeThingResult describeThingResult = client.describeThing(describeThingRequest);
		log.info("DefaultClientId=" + describeThingResult.getDefaultClientId());
		log.info("ThingName=" + describeThingResult.getThingName());
		log.info("Attributes=" + describeThingResult.getAttributes());
	}
}
