package org.yenbo.mbed;

import org.yenbo.commonUtils.PropertiesReader;
import org.yenbo.commonUtils.PropertiesReader.FileType;

public class PropertiesSingleton {

	private static PropertiesSingleton instance;
	
	private PropertiesReader reader;
	
	private PropertiesSingleton() {
		reader = new PropertiesReader("D:/ws-git/documents/infra/mbed/mbeddemo.properties",
				FileType.FILE);
	}

	public static PropertiesSingleton getInstance() {
		
		if (instance == null) {
			instance = new PropertiesSingleton();
		}
		
		return instance;
	}
	
	public String getParam(String key) {
		return reader.getParam(key);
	}
}
