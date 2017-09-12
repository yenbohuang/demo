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

@Provider
@Produces("text/html")
public class OAuthAuthorizationDataMessageBodyWriter implements MessageBodyWriter<OAuthAuthorizationData> {

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
		
		Writer writer = new PrintWriter(entityStream);
        writer.write("<html>");
        writer.write("<body>");
        writer.write("<h2>JAX-RS Message Body Writer Example</h2>");
        writer.write("<p>ClientId: " + oAuthAuthorizationData.getClientId() + "</p>");
        writer.write("</body>");
        writer.write("</html>");

        writer.flush();
        writer.close();
	}

}
