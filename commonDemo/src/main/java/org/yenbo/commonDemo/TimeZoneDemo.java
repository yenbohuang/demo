package org.yenbo.commonDemo;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.DateTimeException;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimeZoneDemo {

	private static final Logger log = LoggerFactory.getLogger(TimeZoneDemo.class);
	
	public static void main(String[] args) {
		
		listTimezoneIds();
		zoneOffset();
	}
	
	private static void listTimezoneIds() {
		
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
		
		// Include group ID: https://en.wikipedia.org/wiki/International_Air_Transport_Association_code#IATA_timezone_codes
		// FIXME This part does not work and will always mapped to GMT.
		timeZoneList.add("IATA~UTC");
		timeZoneList.add("IATA~RU01");
		
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
		
		// Microsoft https://support.microsoft.com/en-us/help/973627/microsoft-time-zone-index-values
		
		List<String> list = new ArrayList<>();
		
		final String pattern = "HKEY_LOCAL_MACHINE\\SOFTWARE\\Microsoft\\Windows NT\\CurrentVersion\\Time Zones\\";
		
		try {
			Process process = Runtime.getRuntime().exec("reg query \"HKLM\\SOFTWARE\\Microsoft\\Windows NT\\CurrentVersion\\Time Zones\"");
			for (String line: IOUtils.readLines(process.getInputStream(), StandardCharsets.UTF_8)) {
				
				if (line.startsWith(pattern)) {
					// FIXME This part does not work and will always mapped to GMT.
					list.add("MICROSOFT~" + line.replace(pattern, ""));
				}
			}
		} catch (IOException ex) {
			log.error(ex.getMessage(), ex);
		}
		
		return list;
	}
	
	private static void zoneOffset() {
		ZoneOffset offset = ZoneOffset.ofHours(-11);
		log.info("Full+English: {}, Id={}, toString={}, CanonicalId={}",
				offset.getDisplayName(TextStyle.FULL, Locale.ENGLISH),
				offset.getId(),
				offset,
				TimeZone.getAvailableIDs(offset.getTotalSeconds() * 1000));
	}
}
