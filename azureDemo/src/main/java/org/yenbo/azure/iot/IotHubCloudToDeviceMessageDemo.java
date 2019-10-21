package org.yenbo.azure.iot;

import java.io.IOException;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.microsoft.azure.sdk.iot.service.DeliveryAcknowledgement;
import com.microsoft.azure.sdk.iot.service.FeedbackBatch;
import com.microsoft.azure.sdk.iot.service.FeedbackReceiver;
import com.microsoft.azure.sdk.iot.service.FeedbackRecord;
import com.microsoft.azure.sdk.iot.service.IotHubServiceClientProtocol;
import com.microsoft.azure.sdk.iot.service.Message;
import com.microsoft.azure.sdk.iot.service.ServiceClient;

public class IotHubCloudToDeviceMessageDemo {

	private static final Logger log = LoggerFactory.getLogger(IotHubCloudToDeviceMessageDemo.class);

	public static void main(String[] args) {

		String iotHubConnectionString = System.getenv("IOT_HUB_CONN_STRING");
		log.info("IOT_HUB_CONN_STRING = {}", iotHubConnectionString);
		
		String edgeDeviceId = System.getenv("EDGE_DEVICE_ID");
		log.info("EDGE_DEVICE_ID = {}", edgeDeviceId);
		
		String edgeModuleId = System.getenv("EDGE_MODULE_ID");
		log.info("EDGE_MODULE_ID = {}", edgeModuleId);

		ServiceClient serviceClient = null;
		FeedbackReceiver feedbackReceiver = null;

		try {
			serviceClient = openServiceClient(iotHubConnectionString);
			feedbackReceiver = openFeedbackReceiver(serviceClient);
						
			Message messageToSend = new Message("hello from yenbo " + Instant.now());
			messageToSend.setDeliveryAcknowledgementFinal(DeliveryAcknowledgement.Full);
			messageToSend.setMessageId(UUID.randomUUID().toString());
			
			log.info("messageToSend.getMessageId() = {}", messageToSend.getMessageId());
			
			// working
			serviceClient.send(edgeDeviceId, messageToSend);
			
			// FIXME not working
//			serviceClient.send(edgeDeviceId, edgeModuleId, messageToSend);
			
			FeedbackBatch feedbackBatch = feedbackReceiver.receive(30000);
			
			if (feedbackBatch != null) {
				
				log.info("feedbackBatch.getEnqueuedTimeUtc() = {}", feedbackBatch.getEnqueuedTimeUtc());
				log.info("feedbackBatch.getRecords().size() = {}", feedbackBatch.getRecords().size());
				
				for (FeedbackRecord feedbackRecord: feedbackBatch.getRecords()) {
					log.info("feedbackRecord.getStatusCode() = {}", feedbackRecord.getStatusCode());
					log.info("feedbackRecord.getDeviceId() = {}", feedbackRecord.getDeviceId());
					log.info("feedbackRecord.getDescription() = {}", feedbackRecord.getDescription());
					log.info("feedbackRecord.getDeviceGenerationId() = {}", feedbackRecord.getDeviceGenerationId());
					log.info("feedbackRecord.getEnqueuedTimeUtc() = {}", feedbackRecord.getEnqueuedTimeUtc());
					log.info("feedbackRecord.getOriginalMessageId() = {}", feedbackRecord.getOriginalMessageId());
				}
				
			} else {
				log.warn("No feedback");
			}

		} catch (Exception ex) {
			log.info(ex.getMessage(), ex);
		} finally {
			closeFeedbackReceiver(feedbackReceiver);
			closeServiceClient(serviceClient);
		}

		log.info("Done!");
	}
	
	private static ServiceClient openServiceClient(String iotHubConnectionString)
			throws IOException, InterruptedException, ExecutionException {
		
        ServiceClient serviceClient = ServiceClient.createFromConnectionString(
        		iotHubConnectionString, IotHubServiceClientProtocol.AMQPS);
        
        serviceClient.open();
        log.info("IoT Hub connected.");
        
        return serviceClient;
    }
	
	private static void closeServiceClient(ServiceClient serviceClient) {
		
		if (serviceClient != null) {
			
			try {
				serviceClient.close();
				log.info("serviceClient closed.");
				
			} catch (Exception ex) {
				log.error(ex.getMessage(), ex);
			}
		}
    }
	
	private static FeedbackReceiver openFeedbackReceiver(ServiceClient serviceClient)
			throws IOException {
		
		FeedbackReceiver feedbackReceiver = null;
		
        if (serviceClient != null) {
            
        	feedbackReceiver = serviceClient.getFeedbackReceiver();
        	feedbackReceiver.open();
        	log.info("Successfully opened FeedbackReceiver.");
        }
        
        return feedbackReceiver;
    }

    private static void closeFeedbackReceiver(FeedbackReceiver feedbackReceiver) {
        
    	if (feedbackReceiver != null) {
    		try {
    			feedbackReceiver.close();
				log.info("Successfully closed FeedbackReceiver.");
			} catch (IOException ex) {
				log.error(ex.getMessage(), ex);
			}
    	}
    }
}
