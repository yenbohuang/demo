package org.yenbo.commonDemo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LocaleDemo {

	private static final Logger log = LoggerFactory.getLogger(LocaleDemo.class);
	
	private LocaleDemo() {}
	
	public static void main(String[] args) {
		printLocaleMethodResults();
		printCountryAndDisplayNames();
		printAvailableLangTags();
	}
	
	private static void printLocaleMethodResults() {
		for (Locale locale: Locale.getAvailableLocales()) {
			// toLanguageTag()="zh-TW", toString()="zh_TW"
			log.info("toLanguageTag()={}, toString()={}", locale.toLanguageTag(), locale);
		}
	}
	
	/**
	 * This method generates a list which can be pasted into Excel easily for documentation usages.
	 */
	private static void printCountryAndDisplayNames() {
		
		System.out.println("Country Code\tDisplay Name");
		for (String country: Locale.getISOCountries()) {
			System.out.format("%s\t%s\n", country, new Locale("en", country).getDisplayCountry());
		}
	}
	
	/**
	 * This method generates a list which can be pasted into Excel easily for documentation usages.
	 */
	private static void printAvailableLangTags() {
		
		ArrayList<Locale> list = new ArrayList<>();
		
		for (Locale locale: Locale.getAvailableLocales()) {
			String tag = locale.toLanguageTag();
			if (5 == tag.length()) {
				list.add(locale);
			}
		}
		
		Locale[] langTagArray = list.toArray(new Locale[0]);
		Arrays.sort(langTagArray, new Comparator<Locale>() {
			@Override
			public int compare(Locale o1, Locale o2) {
				return o1.toLanguageTag().compareTo(o2.toLanguageTag());
			}
		});
		
		System.out.println("Language Tag\tDisplay Name");
		for (Locale locale: langTagArray) {
			System.out.format("%s\t%s\n", locale.toLanguageTag(), locale.getDisplayName());
		}
	}
}
