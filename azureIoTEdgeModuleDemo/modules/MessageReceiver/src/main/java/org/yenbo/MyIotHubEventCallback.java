package org.yenbo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.microsoft.azure.sdk.iot.device.IotHubEventCallback;
import com.microsoft.azure.sdk.iot.device.IotHubStatusCode;

public class MyIotHubEventCallback implements IotHubEventCallback {

	private static final Logger logger = LoggerFactory.getLogger(MyIotHubEventCallback.class);
			
	@Override
	public void execute(IotHubStatusCode responseStatus, Object callbackContext) {
		logger.info("responseStatus.name() = {}", responseStatus.name());
	}

}
