package org.yenbo.jetty.oauth2;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
import org.apache.cxf.rs.security.oauth2.services.ClientRegistrationResponse;
import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Provider
@Produces("application/json")
@Consumes("application/json")
public class OAuthExtensionJSONProvider implements MessageBodyWriter<Object>,
	MessageBodyReader<Object> {

	private static final Logger log = LoggerFactory.getLogger(OAuthExtensionJSONProvider.class);
	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
	
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
		return ClientRegistrationResponse.class == type;
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
	private ClientRegistration fromMapToClientRegistration(InputStream is) throws IOException {
		
		// If we throw OAuthServiceException here, it becomes unhandled.
		// Validate the data elsewhere and ignore unknown properties.
		
		Map<String, Object> params = new JsonMapObjectReaderWriter().fromJson(is);
		ClientRegistration clientRegistration = Oauth2Factory.createClientRegistration();
		Map<String, String> clientNameMap = new HashMap<>();
		
		for (Entry<String, Object> entry: params.entrySet()) {
			
			switch(entry.getKey()) {
			
			case ClientRegistration.CLIENT_NAME:
				clientRegistration.setClientName((String) entry.getValue());
				break;
				
			case ClientRegistration.REDIRECT_URIS:
				clientRegistration.setRedirectUris((List<String>) entry.getValue());
				break;
				
			case ClientRegistration.SCOPE:
				clientRegistration.setScope((String) entry.getValue());
				break;
				
			// other properties
			case OAuthExtensionConstants.CLIENT_DESCRIPTION:
				clientRegistration.setProperty(entry.getKey(), (String) entry.getValue());
				break;
				
			default:
				// client name i18n
				if (entry.getKey().startsWith(OAuthExtensionConstants.CLIENT_NAME_PREFIX)) {
					clientNameMap.put(entry.getKey(), (String) entry.getValue());
				}
				break;
			}
		}
		
		if (!clientNameMap.isEmpty()) {
			clientRegistration.setProperty(OAuthExtensionConstants.CLIENT_NAME_I18N, clientNameMap);
		}
		
		return clientRegistration;
	}

	@Override
	public void writeTo(Object obj, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream)
			throws IOException, WebApplicationException {
		
		if (obj instanceof ClientRegistrationResponse) {
			writeClientRegistrationResponse((ClientRegistrationResponse) obj, entityStream);
		} else {
			log.error("Unknown type: {}", obj);
		}
	}
	
	private void appendArrayNode(ClientRegistrationResponse obj, ObjectNode root, String key) {
		
		List<String> list = obj.getListStringProperty(key);
		if (null != list) {
			ArrayNode arrayNode = OBJECT_MAPPER.createArrayNode();
			for (String str: list) {
				arrayNode.add(str);
			}
			root.set(key, arrayNode);
		}
	}
	
	private void writeClientRegistrationResponse(ClientRegistrationResponse obj, OutputStream os)
			throws IOException {

		// TODO how to handle "client_name#<language tag>" gracefully?
		ObjectNode root = OBJECT_MAPPER.createObjectNode();
		
		root.put(ClientRegistrationResponse.CLIENT_ID, obj.getClientId());
		root.put(ClientRegistrationResponse.CLIENT_ID_ISSUED_AT, obj.getClientIdIssuedAt());
		root.put(ClientRegistrationResponse.CLIENT_SECRET, obj.getClientSecret());
		root.put(ClientRegistration.SCOPE, obj.getStringProperty(ClientRegistration.SCOPE));
		root.put(ClientRegistration.TOKEN_ENDPOINT_AUTH_METHOD,
				obj.getStringProperty(ClientRegistration.TOKEN_ENDPOINT_AUTH_METHOD));
		
		// There is a bug in cxf library that "obj.getClientSecretExpiresAt()" returns null.
		// Set as zero and never expire.
		root.put(ClientRegistrationResponse.CLIENT_SECRET_EXPIRES_AT, 0);
		
		appendArrayNode(obj, root, ClientRegistration.REDIRECT_URIS);
		appendArrayNode(obj, root, ClientRegistration.GRANT_TYPES);
		appendArrayNode(obj, root, ClientRegistration.RESPONSE_TYPES);
		
		root.put(ClientRegistration.CLIENT_NAME, obj.getStringProperty(ClientRegistration.CLIENT_NAME));
		
		// client_name#<language tag>
		Map<String, Object> clientNameI18nMap = obj.getMapProperty(
				OAuthExtensionConstants.CLIENT_NAME_I18N);
		if (null != clientNameI18nMap && !clientNameI18nMap.isEmpty()) {
			for (Entry<String, Object> entry: clientNameI18nMap.entrySet()) {
				root.put(entry.getKey(), (String) entry.getValue());
			}
		}
		
		os.write(OBJECT_MAPPER.writeValueAsBytes(root));
		os.flush();
	}
}
