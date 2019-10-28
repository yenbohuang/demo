package org.yenbo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.microsoft.azure.sdk.iot.device.IotHubConnectionStatusChangeCallback;
import com.microsoft.azure.sdk.iot.device.IotHubConnectionStatusChangeReason;
import com.microsoft.azure.sdk.iot.device.transport.IotHubConnectionStatus;

public class MyConnectionStatusChangeCallback  implements IotHubConnectionStatusChangeCallback {

	private static final Logger logger = LoggerFactory.getLogger(
			MyConnectionStatusChangeCallback.class);
	
    @Override
    public void execute(IotHubConnectionStatus status,
                        IotHubConnectionStatusChangeReason statusChangeReason,
                        Throwable throwable, Object callbackContext) {
    	
        switch (status) {
            case CONNECTED:
                logger.info("Connection Status: Connected");
                break;
            case DISCONNECTED:
                logger.warn("Connection Status: Disconnected", throwable);
                System.exit(1);
                break;
            case DISCONNECTED_RETRYING:
                logger.info("Connection Status: Retrying");
                break;
            default:
                break;
        }
    }
}
