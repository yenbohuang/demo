package org.yenbo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.microsoft.azure.sdk.iot.device.IotHubMessageResult;
import com.microsoft.azure.sdk.iot.device.Message;
import com.microsoft.azure.sdk.iot.device.MessageCallback;
import com.microsoft.azure.sdk.iot.device.ModuleClient;

public class MyMessageCallback implements MessageCallback {
	
	private static final Logger logger = LoggerFactory.getLogger(MyMessageCallback.class);
	
	private MyEventCallback eventCallback;
    private int counter = 0;
    
    public MyMessageCallback(MyEventCallback eventCallback) {
    	this.eventCallback = eventCallback;
    }

    @Override
    public IotHubMessageResult execute(Message msg, Object context) {
        this.counter += 1;

        logger.info("Received message {}: {}", counter,
        		new String(msg.getBytes(), Message.DEFAULT_IOTHUB_MESSAGE_CHARSET));
        
        if (context instanceof ModuleClient) {
            ModuleClient client = (ModuleClient) context;
            client.sendEventAsync(msg, eventCallback, msg, "upstream");
        }
        return IotHubMessageResult.COMPLETE;
    }
}
