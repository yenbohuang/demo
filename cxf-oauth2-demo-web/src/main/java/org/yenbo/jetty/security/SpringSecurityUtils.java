package org.yenbo.jetty.security;

import java.security.Principal;

import org.apache.cxf.jaxrs.ext.MessageContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

public class SpringSecurityUtils {

	private SpringSecurityUtils() {
	}

	public static SavedRequest getSavedRequest(MessageContext messageContext) {

		if (null == messageContext) {
			throw new IllegalArgumentException("messageContext is null");
		}

		return new HttpSessionRequestCache().getRequest(messageContext.getHttpServletRequest(),
				messageContext.getHttpServletResponse());
	}
	
	public static UsernamePasswordAuthenticationToken getUsernamePasswordAuthenticationToken(
			Principal principal) {
		
		if (null == principal) {
			throw new IllegalArgumentException("principal is null");
		}
		
		return (UsernamePasswordAuthenticationToken) principal;
	}
	
	public static UserDetails getUserDetails(Principal principal) {
		return (UserDetails) getUsernamePasswordAuthenticationToken(principal).getPrincipal();
	}
	
	public static WebAuthenticationDetails getWebAuthenticationDetails(Principal principal) {
		return (WebAuthenticationDetails) getUsernamePasswordAuthenticationToken(principal).getDetails();
	}
}
