package org.yenbo.awssdkdemo;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertyReader {

	private static final Logger log = LoggerFactory.getLogger(PropertyReader.class);
	private static final String FILENAME = "D:/ws-git/documents/infra/AWS/awsdemo.properties";
	private static PropertyReader propertyReader;
	
	private HashMap<String, String> paramMap = new HashMap<>();
	
	private PropertyReader() {
	}

	public static final PropertyReader getInstance() {
		
		if (propertyReader == null) {
			propertyReader = new PropertyReader();
		}
		
		return propertyReader;
	}
	
	public String getParam(String key) {
		
		if (StringUtils.isBlank(key)) {
			throw new IllegalArgumentException("key is blank");
		}
		
		String value = null;
		
		if (paramMap.containsKey(key)) {
			
			value = paramMap.get(key);
			
		} else {
			
			try (FileInputStream outputStream = new FileInputStream(FILENAME)) {
				
				Properties prop = new Properties();
				prop.load(outputStream);
				value = prop.getProperty(key);
				paramMap.put(key, value);
				
				if (log.isDebugEnabled()) {
					log.debug("Read from {}: {}={}", FILENAME, key, value);
				}
				
			} catch (IOException e) {
				throw new AwsDemoException(e);
			}
		}
		
		return value;
	}
}
