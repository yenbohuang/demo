package org.yenbo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.microsoft.azure.sdk.iot.device.IotHubEventCallback;
import com.microsoft.azure.sdk.iot.device.IotHubStatusCode;
import com.microsoft.azure.sdk.iot.device.Message;

public class MyEventCallback  implements IotHubEventCallback {
	
	private static final Logger logger = LoggerFactory.getLogger(MyEventCallback.class);
	
    @Override
    public void execute(IotHubStatusCode status, Object context) {
    	
        if (context instanceof Message) {
            logger.info("Send message with status: {}", status.name());
        } else {
            logger.info("Invalid context passed");
        }
    }
}
