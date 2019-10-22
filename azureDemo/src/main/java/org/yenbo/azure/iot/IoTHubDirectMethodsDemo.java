package org.yenbo.azure.iot;

import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.microsoft.azure.sdk.iot.service.devicetwin.DeviceMethod;
import com.microsoft.azure.sdk.iot.service.devicetwin.MethodResult;

public class IoTHubDirectMethodsDemo {

	private static final Logger log = LoggerFactory.getLogger(IoTHubDirectMethodsDemo.class);
	
	private static final long RESPONSE_TIMEOUT_SECS = 30;
	private static final long CONNECT_TIMEOUT_SECS = 5;
	
	public static void main(String[] args) {
		
		String iotHubConnectionString = System.getenv("IOT_HUB_CONN_STRING");
		log.info("IOT_HUB_CONN_STRING = {}", iotHubConnectionString);
		
		String edgeDeviceId = System.getenv("EDGE_DEVICE_ID");
		log.info("EDGE_DEVICE_ID = {}", edgeDeviceId);
		
		String edgeModuleId = System.getenv("EDGE_MODULE_ID");
		log.info("EDGE_MODULE_ID = {}", edgeModuleId);
		
		try {
			DeviceMethod methodClient = DeviceMethod.createFromConnectionString(
					iotHubConnectionString);
			
			// FIXME {"errorCode":404103,"trackingId":"59097ca8db1b42359e59f1d5b6299485-G:10-TimeStamp:10/22/2019 05:06:20-G:11-TimeStamp:10/22/2019 05:06:20","message":"Timed out waiting for device to subscribe.","info":{},"timestampUtc":"2019-10-22T05:06:20.2711113Z"} 
//			MethodResult result = methodClient.invoke(edgeDeviceId, "whoCares",
//					RESPONSE_TIMEOUT_SECS, CONNECT_TIMEOUT_SECS,
//					"DM hello from yenbo " + Instant.now());
			
			// FIXME status = 501, payload = null
			MethodResult result = methodClient.invoke(edgeDeviceId, edgeModuleId,
					"whoCares", RESPONSE_TIMEOUT_SECS, CONNECT_TIMEOUT_SECS,
					"DM hello from yenbo " + Instant.now());
			
			if (result != null) {
				log.info("status = {}, payload = {}", result.getStatus(), result.getPayload());
			} else {
				log.warn("MethodResult is null");
			}
			
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		}
	}
}
