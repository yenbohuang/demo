package org.yenbo.commonDemo;

import java.nio.file.Paths;

public class FileAndPathDemo {

	public static void main(String[] args) {
		
		getCurrentPath();
	}
	
	private static void getCurrentPath() {
		System.out.println("Current relative path is: " + Paths.get("").toAbsolutePath().toString());
	}
}
