package org.yenbo.jetty.oauth2;

import java.util.List;

import org.apache.cxf.rs.security.oauth2.common.AccessTokenRegistration;
import org.apache.cxf.rs.security.oauth2.common.Client;
import org.apache.cxf.rs.security.oauth2.common.OAuthPermission;
import org.apache.cxf.rs.security.oauth2.common.ServerAccessToken;
import org.apache.cxf.rs.security.oauth2.common.UserSubject;
import org.apache.cxf.rs.security.oauth2.provider.OAuthDataProvider;
import org.apache.cxf.rs.security.oauth2.provider.OAuthServiceException;
import org.apache.cxf.rs.security.oauth2.tokens.refresh.RefreshToken;

public class OAuthMemoryDataProvider implements OAuthDataProvider {

	@Override
	public List<OAuthPermission> convertScopeToPermissions(Client client, List<String> requestedScopes) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ServerAccessToken createAccessToken(AccessTokenRegistration accessToken) throws OAuthServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ServerAccessToken getAccessToken(String accessToken) throws OAuthServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ServerAccessToken> getAccessTokens(Client client, UserSubject subject) throws OAuthServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Client getClient(String clientId) throws OAuthServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ServerAccessToken getPreauthorizedToken(Client client, List<String> requestedScopes, UserSubject subject,
			String grantType) throws OAuthServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<RefreshToken> getRefreshTokens(Client client, UserSubject subject) throws OAuthServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ServerAccessToken refreshAccessToken(Client client, String refreshToken, List<String> requestedScopes)
			throws OAuthServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeAccessToken(ServerAccessToken accessToken) throws OAuthServiceException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void revokeToken(Client client, String tokenId, String tokenTypeHint) throws OAuthServiceException {
		// TODO Auto-generated method stub
		
	}

}
