package org.yenbo.jetty.oauth2;

import java.util.List;

import org.apache.cxf.rs.security.oauth2.common.Client;
import org.apache.cxf.rs.security.oauth2.common.ServerAccessToken;
import org.apache.cxf.rs.security.oauth2.common.UserSubject;
import org.apache.cxf.rs.security.oauth2.grants.code.AbstractAuthorizationCodeDataProvider;
import org.apache.cxf.rs.security.oauth2.grants.code.ServerAuthorizationCodeGrant;
import org.apache.cxf.rs.security.oauth2.provider.OAuthServiceException;
import org.apache.cxf.rs.security.oauth2.tokens.refresh.RefreshToken;

public class InMemoryAuthorizationCodeDataProvider extends AbstractAuthorizationCodeDataProvider {

	@Override
	public ServerAuthorizationCodeGrant removeCodeGrant(String code) throws OAuthServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ServerAuthorizationCodeGrant> getCodeGrants(Client c, UserSubject subject)
			throws OAuthServiceException {
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
	public List<RefreshToken> getRefreshTokens(Client client, UserSubject subject) throws OAuthServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setClient(Client client) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Client> getClients(UserSubject resourceOwner) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void saveAccessToken(ServerAccessToken serverToken) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void saveRefreshToken(RefreshToken refreshToken) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void doRevokeAccessToken(ServerAccessToken accessToken) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void doRevokeRefreshToken(RefreshToken refreshToken) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected RefreshToken getRefreshToken(String refreshTokenKey) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Client doGetClient(String clientId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void doRemoveClient(Client c) {
		// TODO Auto-generated method stub
		
	}

}
