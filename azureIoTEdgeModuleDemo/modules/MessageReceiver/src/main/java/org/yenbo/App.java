package org.yenbo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.microsoft.azure.sdk.iot.device.ModuleClient;

public class App {
	
	private static final Logger logger = LoggerFactory.getLogger(App.class);
	
    public static void main(String[] args) {
        
    	for (String key: System.getenv().keySet()) {
    		logger.info("{} = {}", key, System.getenv(key));
    	}
    	
    	MyEventCallback eventCallback = new MyEventCallback();
        MyMessageCallback msgCallback = new MyMessageCallback(eventCallback);
        MyDeviceMethodCallback methodCallback = new MyDeviceMethodCallback();
        MyIotHubEventCallback methodStatusCallback = new MyIotHubEventCallback();
    	
    	try {
            ModuleClient client = ModuleClient.createFromEnvironment();
            
            client.setMessageCallback(msgCallback, client);
            client.registerConnectionStatusChangeCallback(new MyConnectionStatusChangeCallback(),
            		client);
            
            client.open();
            client.subscribeToMethod(methodCallback, client, methodStatusCallback, client);
            
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            System.exit(1);
        }
    }
}
