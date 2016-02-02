package org.yenbo.awssdkdemo;

import org.yenbo.commonUtils.PropertiesReader;
import org.yenbo.commonUtils.PropertiesReader.FileType;

public class PropertiesSingleton {

	private static PropertiesSingleton propertyReader;

	private PropertiesReader reader;
	
	private PropertiesSingleton() {
		reader = new PropertiesReader("D:/ws-git/documents/infra/AWS/awsdemo.properties",
				FileType.FILE);
	}

	public static final PropertiesSingleton getInstance() {
		
		if (propertyReader == null) {
			propertyReader = new PropertiesSingleton();
		}
		
		return propertyReader;
	}
	
	public String getParam(String key) {
		return reader.getParam(key);
	}
}
