package org.yenbo.commonDemo;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.DateTimeException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimeZoneDemo {

	private static final Logger log = LoggerFactory.getLogger(TimeZoneDemo.class);
	
	public static void main(String[] args) {
		
		List<String> timeZoneList = new ArrayList<>();
		timeZoneList.addAll(Arrays.asList(TimeZone.getAvailableIDs()));
		
		// ICU
		timeZoneList.add("Asia/Atyrau");
		timeZoneList.add("Asia/Barnaul");
		timeZoneList.add("Asia/Famagusta");
		timeZoneList.add("Asia/Tomsk");
		timeZoneList.add("Asia/Yangon");
		timeZoneList.add("Europe/Astrakhan");
		timeZoneList.add("Europe/Kirov");
		timeZoneList.add("Europe/Saratov");
		timeZoneList.add("Europe/Ulyanovsk");
		timeZoneList.add("Factory");
		timeZoneList.add("GMT+0");
		timeZoneList.add("GMT-0");
		
		// Include group ID
		timeZoneList.add("IATA~UTC");
		
		// Windows
		timeZoneList.addAll(readWindowsRegistry());
		
		for (String id: timeZoneList) {
			
			try {
				log.info("Original ID={}, ZoneId={}", id, ZoneId.of(id).toString());
			} catch (DateTimeException ex) {
				TimeZone tz = TimeZone.getTimeZone(id);
				log.info("Original ID={}, ZoneId={}, ZoneInfo={}", id, tz.toZoneId(), tz);
			}
		}
	}
	
	private static List<String> readWindowsRegistry() {
		
		// Microsoft https://technet.microsoft.com/en-us/library/cc749073(v=ws.10).aspx
		
		List<String> list = new ArrayList<>();
		
		final String pattern = "HKEY_LOCAL_MACHINE\\SOFTWARE\\Microsoft\\Windows NT\\CurrentVersion\\Time Zones\\";
		
		try {
			Process process = Runtime.getRuntime().exec("reg query \"HKLM\\SOFTWARE\\Microsoft\\Windows NT\\CurrentVersion\\Time Zones\"");
			for (String line: IOUtils.readLines(process.getInputStream(), StandardCharsets.UTF_8)) {
				
				if (line.startsWith(pattern)) {
					list.add("MICROSOFT~" + line.replace(pattern, ""));
				}
			}
		} catch (IOException ex) {
			log.error(ex.getMessage(), ex);
		}
		
		return list;
	}
}
