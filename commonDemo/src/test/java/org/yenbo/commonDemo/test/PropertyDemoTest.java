package org.yenbo.commonDemo.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yenbo.commonDemo.PropertyDemo;

public class PropertyDemoTest {

	private static final Logger log = LoggerFactory.getLogger(PropertyDemoTest.class);
	
	public static void main(String[] args) {
		log.info(PropertyDemo.getInstance().getParam("key1"));
	}
}
