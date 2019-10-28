package org.yenbo;

import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.microsoft.azure.sdk.iot.device.DeviceTwin.DeviceMethodCallback;
import com.microsoft.azure.sdk.iot.device.DeviceTwin.DeviceMethodData;

public class MyDeviceMethodCallback implements DeviceMethodCallback {

	private static final Logger logger = LoggerFactory.getLogger(MyDeviceMethodCallback.class);
	
	private static final int METHOD_SUCCESS = 200;
	private static final int METHOD_NOT_DEFINED = 404;
	private static final int INVALID_PARAMETER = 400;
	
	@Override
	public DeviceMethodData call(String methodName, Object methodData, Object context) {
		
		String payload = new String((byte[])methodData, StandardCharsets.UTF_8);
		logger.info("methodName = {}, methodData = {}", methodName, payload);
		
		return new DeviceMethodData(METHOD_SUCCESS, "Got message: " + payload);
	}

}
