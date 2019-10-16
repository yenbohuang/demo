// FIXME upgrade to the latest mbed SDK

//package org.yenbo.mbed;
//
//import java.net.InetSocketAddress;
//import java.security.cert.Certificate;
//
//import org.eclipse.californium.core.CoapClient;
//import org.eclipse.californium.core.CoapHandler;
//import org.eclipse.californium.core.CoapResponse;
//import org.eclipse.californium.core.WebLink;
//import org.eclipse.californium.core.network.CoapEndpoint;
//import org.eclipse.californium.core.network.config.NetworkConfig;
//import org.eclipse.californium.scandium.DTLSConnector;
//import org.eclipse.californium.scandium.config.DtlsConnectorConfig;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.yenbo.commonUtils.KeyReader;
//import org.yenbo.commonUtils.KeyReader.PrivateKeyType;
//
//public class DeviceConnectorCoapDemo {
//
//	private static final Logger log = LoggerFactory.getLogger(DeviceConnectorCoapDemo.class);
//	
//	private static final String MBED_SERVER_ADDRESS = "coap://api.connector.mbed.com:5684"; 
//	
//	private DTLSConnector dtlsConnector;
//	
//	public DeviceConnectorCoapDemo() throws Exception {
//		
//		initDtlsConnector();
//	}
//	
//	private void initDtlsConnector() throws Exception {
//		
//		KeyReader keyReader = new KeyReader(
//				PropertiesSingleton.getInstance().getParam("device1.clientCertFilePath"),
//				PropertiesSingleton.getInstance().getParam("device1.privateKeyFilePath"),
//				PrivateKeyType.EC);
//		
//		DtlsConnectorConfig.Builder builder = new DtlsConnectorConfig.Builder(
//				new InetSocketAddress(0));
//		builder.setIdentity(keyReader.getPrivateKey(),
//				new Certificate[] {keyReader.getCertificate()}, true);
//		builder.setTrustStore(new Certificate[] {KeyReader.readCertificate(
//				PropertiesSingleton.getInstance().getParam("device1.serverCertFilePath"))});
//				
//		dtlsConnector = new DTLSConnector(builder.build(), null);
//	}
//	
//	public static void main(String[] args) {
//		
//		try {
//			DeviceConnectorCoapDemo demo = new DeviceConnectorCoapDemo();
//			demo.test();
//			
//			Thread.sleep(2000);
//			log.debug("Sleep complete");
//			
//		} catch (Exception ex) {
//			log.error(ex.getMessage(), ex);
//		}
//	}
//	
//	public void test() {
//		
//		CoapClient coapClient = new CoapClient(MBED_SERVER_ADDRESS);
//		coapClient.setEndpoint(new CoapEndpoint(dtlsConnector, NetworkConfig.getStandard()));
//		
//		// TODO java.security.InvalidKeyException: No installed provider supports this key: sun.security.ec.ECPrivateKeyImpl
//		for (WebLink link: coapClient.discover()) {
//			log.info(link.getURI());
//		}
//		
//		coapClient.get(new CoapHandler() {
//			
//			@Override
//			public void onLoad(CoapResponse response) {
//				log.info(response.getResponseText());
//				
//			}
//			
//			@Override
//			public void onError() {
//				log.error("error");
//			}
//		});
//	}
//}
