package org.yenbo.springDemo.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.yenbo.springDemo.handler.WebSocketDemoHandler;

@Configuration
@EnableWebSocket
@ComponentScan("org.yenbo.springDemo")
public class WebSocketConfig implements WebSocketConfigurer {

	private static final Logger log = LoggerFactory.getLogger(WebSocketConfig.class);
	
	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		
		log.info("add websocket '/time'");
		registry.addHandler(webSocketDemoHandler(), "/time");		
	}
	
	@Bean
	public WebSocketDemoHandler webSocketDemoHandler() {
		return new WebSocketDemoHandler();
	}
}
