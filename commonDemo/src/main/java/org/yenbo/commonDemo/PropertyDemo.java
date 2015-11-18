package org.yenbo.commonDemo;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertyDemo {

	private static final Logger log = LoggerFactory.getLogger(PropertyDemo.class);
	private static PropertyDemo instance;
	private static final String FILENAME = "demo.properties";
	
	private HashMap<String, String> paramMap = new HashMap<>();
	
	private PropertyDemo() {
	}

	public static PropertyDemo getInstance() {
		
		if (instance == null) {
			instance = new PropertyDemo();
		}
		
		return instance;
	}
	
	public String getParam(String key) {
		
		if (StringUtils.isBlank(key)) {
			throw new IllegalArgumentException("key is blank");
		}
		
		String value = null;
		
		if (paramMap.containsKey(key)) {
			
			value = paramMap.get(key);
			
		} else {
			
			try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(
					FILENAME)) {
				
				Properties prop = new Properties();
				prop.load(inputStream);
				value = prop.getProperty(key);
				paramMap.put(key, value);
				
				if (log.isDebugEnabled()) {
					log.debug("Read from {}: {}={}", FILENAME, key, value);
				}
				
			} catch (IOException e) {
				throw new CommonDemoException(e);
			}
		}
		
		return value;
	}
}
