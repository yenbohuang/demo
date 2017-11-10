package org.yenbo.jetty.oauth2;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import org.apache.cxf.jaxrs.json.basic.JsonMapObjectReaderWriter;
import org.apache.cxf.rs.security.oauth2.services.ClientRegistration;
import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Provider
@Produces("application/json")
@Consumes("application/json")
public class OAuthExtensionJSONProvider implements MessageBodyWriter<Object>,
	MessageBodyReader<Object> {

	private static final Logger log = LoggerFactory.getLogger(OAuthExtensionJSONProvider.class);
	
	@Override
	public long getSize(Object obj, Class<?> clt, Type t, Annotation[] anns, MediaType mt) {
        return -1;
    }
	
	@Override
	public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return Map.class.isAssignableFrom(type)
				|| ClientRegistration.class.isAssignableFrom(type);
	}
	
	@Override
	public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return ClientRegistration.class == type;
	}

	@Override
	public Object readFrom(Class<Object> type, Type genericType, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, String> httpHeaders, InputStream entityStream)
			throws IOException, WebApplicationException {
		
		if (ClientRegistration.class.isAssignableFrom(type)) {
			return fromMapToClientRegistration(entityStream);
		}
		
		throw new WebApplicationException("Unknown type: " + type,
				HttpStatus.INTERNAL_SERVER_ERROR_500);
	}
	
	@SuppressWarnings("unchecked")
	private Object fromMapToClientRegistration(InputStream is) throws IOException {
		
		// If we throw OAuthServiceException here, it becomes unhandled.
		// Validate the data elsewhere and ignore unknown properties.
		
		Map<String, Object> params = new JsonMapObjectReaderWriter().fromJson(is);
		ClientRegistration clientRegistration = Oauth2Factory.createClientRegistration();
		
		clientRegistration.setClientName((String) params.get(ClientRegistration.CLIENT_NAME));
		clientRegistration.setRedirectUris((List<String>) params.get(
				ClientRegistration.REDIRECT_URIS));
		clientRegistration.setScope((String) params.get(ClientRegistration.SCOPE));

		// other properties
		for (String key: params.keySet()) {
			switch(key) {
			
			case OAuthExtensionConstants.SOFTWARE_ID:
				clientRegistration.setProperty(OAuthExtensionConstants.SOFTWARE_ID,
						(String) params.get(OAuthExtensionConstants.SOFTWARE_ID));
				break;
			
			case OAuthExtensionConstants.CLIENT_DESCRIPTION:
				clientRegistration.setProperty(OAuthExtensionConstants.CLIENT_DESCRIPTION,
						(String) params.get(OAuthExtensionConstants.CLIENT_DESCRIPTION));
				break;
				
			default:
				// client name i18n
				if (key.startsWith(OAuthExtensionConstants.CLIENT_NAME_PREFIX)) {
					clientRegistration.setProperty(key, (String) params.get(key));
				}
			}
		}
		
		return clientRegistration;
	}

	@Override
	public void writeTo(Object t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream)
			throws IOException, WebApplicationException {
		// TODO Auto-generated method stub
		log.debug("WriteTo: {}", type);
	}	
}
