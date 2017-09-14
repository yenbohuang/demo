package org.yenbo.jetty.oauth2;

import java.util.HashSet;
import java.util.Set;

import org.apache.cxf.rs.security.oauth2.grants.code.ServerAuthorizationCodeGrant;

public class AuthorizationCodeRepository {

	private Set<ServerAuthorizationCodeGrant> grantSet = new HashSet<>();
	
	public void saveAuthorizationCode(ServerAuthorizationCodeGrant grant) {
		grantSet.add(grant);
	}
	
	public ServerAuthorizationCodeGrant getAuthorizationCode(String authorizationCode) {
		
		for (ServerAuthorizationCodeGrant grant: grantSet) {
			if (grant.getCode().equals(authorizationCode)) {
				return grant;
			}
		}
		
		return null;
	}
	
	public void deleteAuthorizationCode(ServerAuthorizationCodeGrant grant) {
		grantSet.remove(grant);
	}
}
