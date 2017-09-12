package org.yenbo.jetty.oauth2;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import org.apache.cxf.rs.security.oauth2.common.OAuthAuthorizationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Provider
@Produces("text/html")
public class OAuthAuthorizationDataMessageBodyWriter implements MessageBodyWriter<OAuthAuthorizationData> {

	@Autowired
	private TemplateEngine templateEngine;
	
	@Override
	public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return type == OAuthAuthorizationData.class;
	}

	@Override
	public long getSize(OAuthAuthorizationData t, Class<?> type, Type genericType, Annotation[] annotations,
			MediaType mediaType) {
		return 0;
	}

	@Override
	public void writeTo(OAuthAuthorizationData oAuthAuthorizationData, Class<?> type, Type genericType, Annotation[] annotations,
			MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream)
			throws IOException, WebApplicationException {
		
		Context context = new Context();
		context.setVariable("clientId", oAuthAuthorizationData.getClientId());
		
		Writer writer = new PrintWriter(entityStream);
        writer.write(templateEngine.process("html/OAuthAuthorizationData.html", context));
        writer.flush();
        writer.close();
	}

}
