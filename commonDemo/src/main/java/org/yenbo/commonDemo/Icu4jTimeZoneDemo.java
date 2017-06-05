package org.yenbo.commonDemo;

import java.time.ZoneId;
import java.time.zone.ZoneRulesException;
import java.util.Arrays;
import java.util.Locale;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.icu.util.TimeZone;
import com.ibm.icu.util.TimeZone.SystemTimeZoneType;

public class Icu4jTimeZoneDemo {

	private static final Logger log = LoggerFactory.getLogger(Icu4jTimeZoneDemo.class);
	
	private Icu4jTimeZoneDemo() {
	}
	
	public static void main(String[] args) {
		
		for (String country: Locale.getISOCountries()) {
		
			Set<String> anyTimeZones = TimeZone.getAvailableIDs(SystemTimeZoneType.ANY, country, null);
			Set<String> canoicalTimeZones = TimeZone.getAvailableIDs(SystemTimeZoneType.CANONICAL, country, null);
			Set<String> canoicalLocationTimeZones = TimeZone.getAvailableIDs(SystemTimeZoneType.CANONICAL_LOCATION, country, null);
			String[] timezoneIds = TimeZone.getAvailableIDs(country);
			
			log.info("Country={}", country);
			log.info("Number of ids: ANY={}, CANONICAL={}, CANONICAL_LOCATION={}, icuIds={}",
					anyTimeZones.size(), canoicalTimeZones.size(), canoicalLocationTimeZones.size(), timezoneIds.length);
			
			if (canoicalLocationTimeZones.size() != canoicalTimeZones.size()) {
				// The result: sizes are always match.
				log.warn("Size of CANONICAL and CANONICAL_LOCATION not match");
			}
			
			// select first timezone
			if (canoicalTimeZones.isEmpty()) {
				log.error("List is empty");
			} else {
				String[] zoneIdArray = canoicalTimeZones.toArray(new String[0]);
				compareNormalized(zoneIdArray);
				Arrays.sort(zoneIdArray);
				log.info("Default timezone={}", ZoneId.of(zoneIdArray[0]).getId());
			}
			
			// check of ZoneId throws exceptions
			for (String id: canoicalLocationTimeZones) {
				checkByZoneId(id);
			}
			
			for (String id: canoicalTimeZones) {
				checkByZoneId(id);
			}
			
			for (String id: anyTimeZones) {
				// Only this one throws exceptions
				checkByZoneId(id);
			}
		}
		
		canonicalTimezones();
	}
	
	private static void checkByZoneId(String zoneId) {
		try {
			ZoneId.of(zoneId).getId();
		} catch (ZoneRulesException ex) {
			log.info("ICU4J canonical ID for '{}' is '{}'", zoneId, TimeZone.getCanonicalID(zoneId));
			log.error(ex.getMessage(), ex);
		}
	}
	
	private static void compareNormalized(String[] zoneIdArray) {
		for (String id: zoneIdArray) {
			String original = ZoneId.of(id).getId();
			String normalized = ZoneId.of(id).normalized().getId();
			
			if (!original.equals(normalized)) {
				// The result: all of them are identical.
				log.info("Normalized timezone ID is different: original={}, normalized={}", original, normalized);
			}
		}
	}
	
	private static void canonicalTimezones() {
		
		for (String zoneId: TimeZone.getAvailableIDs()) {
			
			String canonical = TimeZone.getCanonicalID(zoneId);
			
			if (!zoneId.equals(canonical)) {
				log.info("Non-canonical: {}, canonical: {}", zoneId, canonical);
//				System.out.format("Non-canonical: %s, canonical: %s\n", zoneId, canonical);
			}
		}
	}
}