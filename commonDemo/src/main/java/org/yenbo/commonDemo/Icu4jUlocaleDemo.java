package org.yenbo.commonDemo;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.icu.util.ULocale;

public class Icu4jUlocaleDemo {

	private static final Logger log = LoggerFactory.getLogger(Icu4jUlocaleDemo.class);
	
	private Icu4jUlocaleDemo() {}
	
	public static void main(String[] args) {
		compareIsoCountries();
		compareIsoLanguages();
	}
	
	private static void compareIsoCountries() {
		
		HashSet<String> jdkSet = new HashSet<>();
		jdkSet.addAll(Arrays.asList(Locale.getISOCountries()));
		
		HashSet<String> icu4jSet = new HashSet<>();
		icu4jSet.addAll(Arrays.asList(ULocale.getISOCountries()));
		
		log.info("Country code list size: jdk={}, icu4j={}", jdkSet.size(), icu4jSet.size());
		
		jdkSet.removeAll(icu4jSet);
		// AN is missing
		for (String country: jdkSet) {
			log.info("Missing country code in ICU4J: {}", country);
		}
	}
	
	private static void compareIsoLanguages() {
		
		HashSet<String> jdkSet = new HashSet<>();
		for (String lang: Locale.getISOLanguages()) {
			if (2 == lang.length()) {
				jdkSet.add(lang);
			}
		}
		
		HashSet<String> icu4jSet = new HashSet<>();
		for (String lang: ULocale.getISOLanguages()) {
			if (2 == lang.length()) {
				icu4jSet.add(lang);
			}
		}
		
		log.info("Language list size: jdk={}, icu4j={}", jdkSet.size(), icu4jSet.size());
		
		jdkSet.removeAll(icu4jSet);
		// in, iw, ji are missing
		for (String country: jdkSet) {
			log.info("Missing language in ICU4J: {}", country);
		}
	}
}
