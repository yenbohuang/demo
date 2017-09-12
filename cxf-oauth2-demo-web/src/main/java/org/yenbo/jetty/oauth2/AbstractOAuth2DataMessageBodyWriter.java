package org.yenbo.jetty.oauth2;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

public abstract class AbstractOAuth2DataMessageBodyWriter<T> implements MessageBodyWriter<T> {

	@Autowired
	protected TemplateEngine templateEngine;
	
	private String template;
	
	protected abstract Context createContext(T data);
	
	public String getTemplate() {
		return template;
	}
	
	public void setTemplate(String template) {
		this.template = template;
	}
	
	@Override
	public long getSize(T t, Class<?> type, Type genericType, Annotation[] annotations,
			MediaType mediaType) {
		return 0;
	}
	
	@Override
	public void writeTo(T data, Class<?> type,
			Type genericType, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream)
			throws IOException, WebApplicationException {

		if (StringUtils.isBlank(template)) {
			throw new WebApplicationException("template is blank.");
		}
		
		Writer writer = new PrintWriter(entityStream);
        writer.write(templateEngine.process(String.format("html/%s.html", template),
        		createContext(data)));
        writer.flush();
        writer.close();
	}
}
