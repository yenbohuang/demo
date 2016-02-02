package org.yenbo.commonDemo;

import org.yenbo.commonUtils.PropertiesReader;
import org.yenbo.commonUtils.PropertiesReader.FileType;

public class PropertyDemo {

	public static void main(String[] args) {
		
		PropertiesReader reader = new PropertiesReader("demo.properties", FileType.JAR);
		System.out.println(reader.getParam("key1"));
	}
}
