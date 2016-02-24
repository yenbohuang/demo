package org.yenbo.commonDemo;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimeDemo {
	
	private static final Logger log = LoggerFactory.getLogger(TimeDemo.class);
	
	public static void main(String[] args) {
		
		log.info(ZonedDateTime.now().toString());
		log.info(ZonedDateTime.now(ZoneId.of("UTC")).toString());
		log.info(Long.toString(ZonedDateTime.now().toEpochSecond()));
		log.info(Instant.ofEpochMilli(1456140744L * 1000L).toString());
	}
}
