package org.yenbo.jetty.spring;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Component
public class Oauth2AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
	
	private static final Logger log = LoggerFactory.getLogger(Oauth2AuthenticationFailureHandler.class);
	
	private String authorizeUrl;
	
	public String getAuthorizeUrl() {
		return authorizeUrl;
	}
	
	public void setAuthorizeUrl(String authorizeUrl) {
		this.authorizeUrl = authorizeUrl;
	}
	
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		
		URIBuilder builder = new URIBuilder();
		builder.setPath("/hello");
		
		log.info(builder.toString());
		super.onAuthenticationFailure(request, response, exception);
	}
}
