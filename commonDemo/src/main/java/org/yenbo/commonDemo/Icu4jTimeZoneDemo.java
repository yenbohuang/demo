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
		
		compareSystemTimeZoneType();
		firstTimezone();
		checkExceptions();
		canonicalizeTimezones();
		canonicalTimezoneByCountry();
	}
	
	private static void compareSystemTimeZoneType() {
		
		for (String country: Locale.getISOCountries()) {
			
			Set<String> anyTimeZones = TimeZone.getAvailableIDs(SystemTimeZoneType.ANY, country, null);
			Set<String> canoicalTimeZones = TimeZone.getAvailableIDs(SystemTimeZoneType.CANONICAL, country, null);
			Set<String> canoicalLocationTimeZones = TimeZone.getAvailableIDs(SystemTimeZoneType.CANONICAL_LOCATION, country, null);
			String[] timezoneIds = TimeZone.getAvailableIDs(country);
			
			log.info("Country={}, Number of ids: ANY={}\t CANONICAL={}\t CANONICAL_LOCATION={}\t icuIds={}",
					country, anyTimeZones.size(), canoicalTimeZones.size(), canoicalLocationTimeZones.size(), timezoneIds.length);
			
			if (canoicalLocationTimeZones.size() != canoicalTimeZones.size()) {
				// The result: sizes are always match.
				log.warn("Size of CANONICAL and CANONICAL_LOCATION not match: country={}", country);
			}
		}
	}
	
	private static void firstTimezone() {
		
		for (String country: Locale.getISOCountries()) {
			
			Set<String> canoicalTimeZones = TimeZone.getAvailableIDs(SystemTimeZoneType.CANONICAL, country, null);
						
			// select first timezone
			if (canoicalTimeZones.isEmpty()) {
				log.error("List is empty: country={}", country);
			} else {
				String[] zoneIdArray = canoicalTimeZones.toArray(new String[0]);
				compareNormalized(zoneIdArray);
				Arrays.sort(zoneIdArray);
				log.info("Country={}\t First timezone={}", country, ZoneId.of(zoneIdArray[0]).getId());
			}
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
	
	private static void checkExceptions() {
		
		for (String country: Locale.getISOCountries()) {
			
			checkByZoneId(TimeZone.getAvailableIDs(SystemTimeZoneType.CANONICAL_LOCATION, country, null));
			checkByZoneId(TimeZone.getAvailableIDs(SystemTimeZoneType.CANONICAL, country, null));
			
			// Only this one throws exceptions
			checkByZoneId(TimeZone.getAvailableIDs(SystemTimeZoneType.ANY, country, null));
		}
	}
	
	private static void checkByZoneId(Set<String> zoneSet) {
		
		for (String zoneId: zoneSet) {
			try {
				ZoneId.of(zoneId).getId();
			} catch (ZoneRulesException ex) {
				log.info("ICU4J canonical ID for '{}' is '{}'", zoneId, TimeZone.getCanonicalID(zoneId));
				log.error(ex.getMessage(), ex);
			}
		}
	}
	
	/**
	 * This method generates a list which can be pasted into Excel easily for documentation usages.
	 */
	private static void canonicalizeTimezones() {
		
		System.out.format("Non-Canonical\tCanonical%n");
		
		for (String zoneId: TimeZone.getAvailableIDs()) {
			
			String canonical = TimeZone.getCanonicalID(zoneId);
			
			if (!zoneId.equals(canonical)) {
				System.out.format("%s\t%s%n", zoneId, canonical);
			}
		}
	}
	
	/**
	 * This method generates a list which can be pasted into Excel easily for documentation usages.
	 */
	private static void canonicalTimezoneByCountry() {
		
		for (String country: Locale.getISOCountries()) {
			
			System.out.format("%n%s (%s)%n", country, new Locale("en", country).getDisplayCountry());

			Set<String> canoicalTimeZones = TimeZone.getAvailableIDs(SystemTimeZoneType.CANONICAL, country, null);
			
			if (canoicalTimeZones.isEmpty()) {
				System.out.format("\t1. %s%n", TimeZone.GMT_ZONE.getID());
			} else {
				String[] zoneIdArray = canoicalTimeZones.toArray(new String[0]);
				Arrays.sort(zoneIdArray);
				for (int i = 0; i < zoneIdArray.length; i++) {
					System.out.format("\t%d. %s%n", i + 1, zoneIdArray[i]);
				}
			}
		}
	}
}
