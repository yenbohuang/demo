package org.yenbo.jetty.oauth2;

import java.util.Locale;

public class ScopeRepository {

	public String getScopeDescription(String scope, Locale locale) {
		return "Description for " + scope;
	}
}
