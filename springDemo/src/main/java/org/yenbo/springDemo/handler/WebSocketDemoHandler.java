package org.yenbo.springDemo.handler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class WebSocketDemoHandler extends TextWebSocketHandler {

	private static final Logger log = LoggerFactory.getLogger(WebSocketDemoHandler.class);
	
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message)
			throws Exception {
		
		log.info(message.getPayload());
		
		while (session.isOpen()) {
			Thread.sleep(2000);
			
			String msg = ZonedDateTime.now(ZoneId.of("UTC")).toString();
			
			session.sendMessage(new TextMessage(msg));
			log.info("Send message: " + msg);
		}
		
		log.info("End while loop");
	}
	
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		log.debug("Connection closed. sessionId={}, status={}", session.getId(), status.toString());
	}
	
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		log.debug("Connection established: sessionId=" + session.getId());
	}
}
