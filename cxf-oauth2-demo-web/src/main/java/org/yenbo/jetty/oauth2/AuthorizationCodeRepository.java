package org.yenbo.jetty.oauth2;

import java.util.HashSet;
import java.util.Set;

import org.apache.cxf.rs.security.oauth2.grants.code.ServerAuthorizationCodeGrant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AuthorizationCodeRepository {

	private static final Logger log = LoggerFactory.getLogger(AuthorizationCodeRepository.class);
	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
	
	private Set<ServerAuthorizationCodeGrant> grantSet = new HashSet<>();
	
	public void saveAuthorizationCode(ServerAuthorizationCodeGrant grant) {
		
		grantSet.add(grant);
		
		try {
			log.debug("{}", OBJECT_MAPPER.writeValueAsString(grant));
		} catch (JsonProcessingException e) {
			log.error(e.getMessage(), e);
		}
	}
	
	public ServerAuthorizationCodeGrant getAuthorizationCode(String authorizationCode) {
		
		for (ServerAuthorizationCodeGrant grant: grantSet) {
			if (grant.getCode().equals(authorizationCode)) {
				try {
					log.debug("{}", OBJECT_MAPPER.writeValueAsString(grant));
				} catch (JsonProcessingException e) {
					log.error(e.getMessage(), e);
				}
				return grant;
			}
		}
		
		log.debug("Not found: {}", authorizationCode);
		return null;
	}
	
	public void deleteAuthorizationCode(ServerAuthorizationCodeGrant grant) {
		
		if (grantSet.remove(grant)) {
			try {
				log.debug("{}", OBJECT_MAPPER.writeValueAsString(grant));
			} catch (JsonProcessingException e) {
				log.error(e.getMessage(), e);
			}
		} else {
			log.debug("Not found: {}", grant.getCode());
		}
	}
}
