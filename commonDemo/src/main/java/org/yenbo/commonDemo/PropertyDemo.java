package org.yenbo.commonDemo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yenbo.commonUtils.PropertiesReader;
import org.yenbo.commonUtils.PropertiesReader.FileType;

public class PropertyDemo {

	private static final Logger log = LoggerFactory.getLogger(PropertyDemo.class);
	
	public static void main(String[] args) {
		
		PropertiesReader reader = new PropertiesReader("demo.properties", FileType.JAR);
		log.info(reader.getParam("key1"));
	}
}
