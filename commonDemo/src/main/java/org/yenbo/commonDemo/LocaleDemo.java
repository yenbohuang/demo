package org.yenbo.commonDemo;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LocaleDemo {

	private static final Logger log = LoggerFactory.getLogger(LocaleDemo.class);
	
	private LocaleDemo() {}
	
	public static void main(String[] args) {
		
		for (Locale locale: Locale.getAvailableLocales()) {
			// toLanguageTag()="zh-TW", toString()="zh_TW"
			log.info("toLanguageTag()={}, toString()={}", locale.toLanguageTag(), locale);
		}
	}
}
