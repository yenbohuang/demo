package org.yenbo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.microsoft.azure.sdk.iot.device.IotHubConnectionStatusChangeCallback;
import com.microsoft.azure.sdk.iot.device.IotHubConnectionStatusChangeReason;
import com.microsoft.azure.sdk.iot.device.IotHubEventCallback;
import com.microsoft.azure.sdk.iot.device.IotHubMessageResult;
import com.microsoft.azure.sdk.iot.device.IotHubStatusCode;
import com.microsoft.azure.sdk.iot.device.Message;
import com.microsoft.azure.sdk.iot.device.MessageCallback;
import com.microsoft.azure.sdk.iot.device.ModuleClient;
import com.microsoft.azure.sdk.iot.device.transport.IotHubConnectionStatus;

public class App {
	
	private static final Logger logger = LoggerFactory.getLogger(App.class);
	
    private static MessageCallbackMqtt msgCallback = new MessageCallbackMqtt();
    private static EventCallback eventCallback = new EventCallback();
    private static final String INPUT_NAME = "input1";
    private static final String OUTPUT_NAME = "output1";

    protected static class EventCallback implements IotHubEventCallback {
        @Override
        public void execute(IotHubStatusCode status, Object context) {
            if (context instanceof Message) {
                logger.info("Send message with status: {}", status.name());
            } else {
                logger.info("Invalid context passed");
            }
        }
    }

    protected static class MessageCallbackMqtt implements MessageCallback {
        private int counter = 0;

        @Override
        public IotHubMessageResult execute(Message msg, Object context) {
            this.counter += 1;

            logger.info("Received message {}: {}", counter,
            		new String(msg.getBytes(), Message.DEFAULT_IOTHUB_MESSAGE_CHARSET));
            if (context instanceof ModuleClient) {
                ModuleClient client = (ModuleClient) context;
                client.sendEventAsync(msg, eventCallback, msg, App.OUTPUT_NAME);
            }
            return IotHubMessageResult.COMPLETE;
        }
    }

    protected static class ConnectionStatusChangeCallback implements IotHubConnectionStatusChangeCallback {

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

    public static void main(String[] args) {
        try {
        	for (String key: System.getenv().keySet()) {
        		logger.info("{} = {}", key, System.getenv(key));
        	}
        	
            ModuleClient client = ModuleClient.createFromEnvironment();
            client.setMessageCallback(App.INPUT_NAME, msgCallback, client);
            client.registerConnectionStatusChangeCallback(new ConnectionStatusChangeCallback(), null);
            client.open();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            System.exit(1);
        }
    }
}
