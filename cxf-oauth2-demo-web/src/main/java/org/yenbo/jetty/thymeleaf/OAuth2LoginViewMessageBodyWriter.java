package org.yenbo.jetty.thymeleaf;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Locale;

import javax.ws.rs.core.MediaType;

import org.thymeleaf.context.Context;
import org.yenbo.jetty.view.OAuth2LoginView;

public class OAuth2LoginViewMessageBodyWriter extends AbstractThymeleafMessageBodyWriter<OAuth2LoginView> {

	public OAuth2LoginViewMessageBodyWriter() {
		super(OAuth2LoginView.class.getSimpleName());
	}

	@Override
	public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return type == OAuth2LoginView.class;
	}

	@Override
	protected Context createContext(OAuth2LoginView data, Locale locale) {
		
		Context context = new Context(locale);
		context.setVariable("isError", data.isError());
		context.setVariable("appName", data.getAppName());
		return context;
	}

}
