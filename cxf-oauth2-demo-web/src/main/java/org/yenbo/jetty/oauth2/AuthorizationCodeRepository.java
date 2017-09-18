package org.yenbo.jetty.oauth2;

import java.util.HashSet;
import java.util.Set;

import org.apache.cxf.rs.security.oauth2.grants.code.ServerAuthorizationCodeGrant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthorizationCodeRepository {

	private static final Logger log = LoggerFactory.getLogger(AuthorizationCodeRepository.class);
	
	private Set<ServerAuthorizationCodeGrant> grantSet = new HashSet<>();
	
	public void saveAuthorizationCode(ServerAuthorizationCodeGrant grant) {
		grantSet.add(grant);
		log.debug("Save: code={}", grant.getCode());
	}
	
	public ServerAuthorizationCodeGrant getAuthorizationCode(String authorizationCode) {
		
		for (ServerAuthorizationCodeGrant grant: grantSet) {
			if (grant.getCode().equals(authorizationCode)) {
				log.debug("Found: {}", authorizationCode);
				return grant;
			}
		}
		
		log.debug("Not found: {}", authorizationCode);
		return null;
	}
	
	public void deleteAuthorizationCode(ServerAuthorizationCodeGrant grant) {
		if (grantSet.remove(grant)) {
			log.debug("Deleted: {}", grant.getCode());
		} else {
			log.debug("Not found: {}", grant.getCode());
		}
	}
}
