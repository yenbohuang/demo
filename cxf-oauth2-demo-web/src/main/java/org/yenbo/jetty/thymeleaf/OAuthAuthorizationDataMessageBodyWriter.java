package org.yenbo.jetty.thymeleaf;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

import org.apache.cxf.rs.security.oauth2.common.OAuthAuthorizationData;
import org.thymeleaf.context.Context;

@Provider
@Produces("text/html")
public class OAuthAuthorizationDataMessageBodyWriter
	extends AbstractThymeleafMessageBodyWriter<OAuthAuthorizationData> {
	
	@Override
	public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return type == OAuthAuthorizationData.class;
	}

	@Override
	protected Context createContext(OAuthAuthorizationData oAuthAuthorizationData) {
		
		Context context = new Context();
		
		// required fields
		context.setVariable("responseType", oAuthAuthorizationData.getResponseType());
		context.setVariable("clientId", oAuthAuthorizationData.getClientId());
		context.setVariable("redirectUri", oAuthAuthorizationData.getRedirectUri());
		context.setVariable("scope", oAuthAuthorizationData.getProposedScope());
		context.setVariable("state", oAuthAuthorizationData.getState());
		context.setVariable("authenticityToken", oAuthAuthorizationData.getAuthenticityToken());
		
		// Display only fields
		context.setVariable("appDescription", oAuthAuthorizationData.getApplicationDescription());
		context.setVariable("appName", oAuthAuthorizationData.getApplicationName());
		context.setVariable("endUserName", oAuthAuthorizationData.getEndUserName());
		context.setVariable("replyTo", oAuthAuthorizationData.getReplyTo());
		
		return context;
	}
}
