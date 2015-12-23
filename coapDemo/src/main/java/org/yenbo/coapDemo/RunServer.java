package org.yenbo.coapDemo;

import org.eclipse.californium.core.CoapServer;

public class RunServer {

	public static void main(String[] args) {
		
		CoapServer server = new CoapServer();
		server.add(new CoapDemoResource("coapDemo"));
		server.start();
	}
}
