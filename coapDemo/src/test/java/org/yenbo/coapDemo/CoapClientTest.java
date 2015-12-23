package org.yenbo.coapDemo;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.WebLink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CoapClientTest {

	private static final Logger log = LoggerFactory.getLogger(CoapClientTest.class);
	
	public static void main(String[] args) {
		
		try {
			
			if (ping()) {
				discover();
				getSync();
				getAsync();
			}
			
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		}
	}
	
	private static void printResponse(CoapResponse response) {
		
		log.info("Code: {}", response.getCode().toString());
		log.info("Payload: {}", response.getResponseText());
	}
	
	public static void getSync() {
		
		log.info("--- Synchronous GET ----");
		
		CoapClient client = new CoapClient("coap://localhost:5683/coapDemo");
		log.info("URI: {}", client.getURI());
		printResponse(client.get());
	}
	
	public static void getAsync() throws InterruptedException {
		
		log.info("--- Asynchronous GET ----");
		
		CoapClient client = new CoapClient("coap://localhost:5683/coapDemo");
		log.info("URI: {}", client.getURI());
		
		client.get(new CoapHandler() {
			
			@Override
			public void onLoad(CoapResponse response) {
				printResponse(response);
			}
			
			@Override
			public void onError() {
				log.error("Failed");	
			}
		});
		
		log.info("sleep for 1 second");
		Thread.sleep(1000);
		
		log.info("sleep ended");
	}
	
	public static void discover() {
		
		log.info("--- Discovery ----");
		
		CoapClient client = new CoapClient("localhost");
		
		for (WebLink link: client.discover()) {
			
			log.info("WebLink URI: {}", link.getURI());
			link.getAttributes().getInterfaceDescriptions();
			
			log.info("WebLink Attribute ContentTypes: {}",
					link.getAttributes().getContentTypes().toString());
			log.info("WebLink Attribute keys: {}",
					link.getAttributes().getAttributeKeySet().toString());
			log.info("WebLink Attribute Count: {}",
					link.getAttributes().getCount());
			log.info("WebLink Attribute Interface Descriptions: {}",
					link.getAttributes().getInterfaceDescriptions().toString());
			log.info("WebLink Attribute MaximumSizeEstimate: {}",
					link.getAttributes().getMaximumSizeEstimate());
			log.info("WebLink Attribute ResourceTypes: {}",
					link.getAttributes().getResourceTypes().toString());
			log.info("WebLink Attribute Title: {}",
					link.getAttributes().getTitle());
		}
	}
	
	public static boolean ping() {
		
		log.info("--- Ping ----");
		
		CoapClient client = new CoapClient("localhost");
		boolean result = client.ping();
		
		log.info("Ping: {}", result);
		return result;
	}
}
