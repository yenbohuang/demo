package org.yenbo.awssdkdemo.iot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yenbo.awssdkdemo.PropertyReader;

import com.amazonaws.services.iot.AWSIotClient;
import com.amazonaws.services.iot.model.CreateKeysAndCertificateRequest;
import com.amazonaws.services.iot.model.CreateKeysAndCertificateResult;
import com.amazonaws.services.iot.model.CreateThingRequest;
import com.amazonaws.services.iot.model.CreateThingResult;
import com.amazonaws.services.iot.model.DescribeCertificateRequest;
import com.amazonaws.services.iot.model.DescribeCertificateResult;
import com.amazonaws.services.iot.model.DescribeThingRequest;
import com.amazonaws.services.iot.model.DescribeThingResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class AwsIotDemoConsole {

	private static final Logger log = LoggerFactory.getLogger(AwsIotDemoConsole.class);
	
	private static AWSIotClient client = new AWSIotClient();
	private static Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
	
	public static void main(String[] args) {
		
		readProperties();
		
		describeThing();
		describeCertificate();
	}
	
	private static void readProperties() {
		
		log.info("---- readProperties ----");
		
		PropertyReader.getInstance().getParam("iot.thingName");
		PropertyReader.getInstance().getParam("iot.thingArn");
		PropertyReader.getInstance().getParam("iot.certificateArn");
		PropertyReader.getInstance().getParam("iot.certificateId");
	}
	
	public static CreateThingResult createThing() {
		
		log.info("---- createThing ----");
		
		CreateThingRequest request = new CreateThingRequest();
		request.setThingName(PropertyReader.getInstance().getParam("iot.thingName"));
		
		CreateThingResult result = client.createThing(request);
		
		log.info(gson.toJson(result));
		return result;
	}
	
	public static void describeThing() {
		
		log.info("---- describeThing ----");
		
		DescribeThingRequest request = new DescribeThingRequest();
		request.setThingName(PropertyReader.getInstance().getParam("iot.thingName"));
		
		DescribeThingResult result = client.describeThing(request);
		
		log.info(gson.toJson(result));
	}
	
	public static void createKeysAndCertificate() {
		
		log.info("---- createKeysAndCertificate ----");
		
		CreateKeysAndCertificateRequest request = new CreateKeysAndCertificateRequest();
		request.setSetAsActive(true);
		
		CreateKeysAndCertificateResult result = client.createKeysAndCertificate(request);
		
		log.info(gson.toJson(result));
	}
	
	public static DescribeCertificateResult describeCertificate() {
		
		log.info("---- describeCertificate ----");
		
		DescribeCertificateRequest request = new DescribeCertificateRequest();
		request.setCertificateId(PropertyReader.getInstance().getParam("iot.certificateId"));
		
		DescribeCertificateResult result = client.describeCertificate(request);
		
		log.info(gson.toJson(result));
		return result;
	}
}
