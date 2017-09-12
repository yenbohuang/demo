package org.yenbo.jetty.oauth2;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

import org.apache.cxf.rs.security.oauth2.common.OAuthError;
import org.thymeleaf.context.Context;

@Provider
@Produces("text/html")
public class OAuthErrorMessageBodyWriter extends AbstractOAuth2DataMessageBodyWriter<OAuthError> {

	@Override
	public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return type == OAuthError.class;
	}

	@Override
	protected Context createContext(OAuthError data) {
		
		Context context = new Context();
		context.setVariable("error", data.getError());
		context.setVariable("errorDescription", data.getErrorDescription());
		context.setVariable("errorUri", data.getErrorUri());
		context.setVariable("state", data.getState());
		return context;
	}

}
