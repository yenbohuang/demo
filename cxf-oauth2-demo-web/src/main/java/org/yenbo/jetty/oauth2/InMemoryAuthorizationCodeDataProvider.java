package org.yenbo.jetty.oauth2;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.apache.cxf.rs.security.oauth2.common.Client;
import org.apache.cxf.rs.security.oauth2.common.ServerAccessToken;
import org.apache.cxf.rs.security.oauth2.common.UserSubject;
import org.apache.cxf.rs.security.oauth2.grants.code.AbstractAuthorizationCodeDataProvider;
import org.apache.cxf.rs.security.oauth2.grants.code.ServerAuthorizationCodeGrant;
import org.apache.cxf.rs.security.oauth2.provider.OAuthServiceException;
import org.apache.cxf.rs.security.oauth2.tokens.refresh.RefreshToken;
import org.apache.cxf.rs.security.oauth2.utils.OAuthConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.yenbo.jetty.domain.InMemoryClient;

@Component
public class InMemoryAuthorizationCodeDataProvider extends AbstractAuthorizationCodeDataProvider {

	private static final Logger log = LoggerFactory.getLogger(InMemoryAuthorizationCodeDataProvider.class);
	
	private InMemoryClient inMemoryClients;

	public InMemoryAuthorizationCodeDataProvider() {
		super();
    	
		inMemoryClients = new InMemoryClient();
    	
    	// this is hardcoded for demo.
		inMemoryClients.setClientId(UUID.fromString("78fa6a41-aec6-4690-9237-7cd6bb6e1a84"));
		inMemoryClients.setClientSecret("7cd6bb6e1a84");
		inMemoryClients.setRedirectUri("http://localhost:8080/api/demo");
    	
    	// copy this line from log file and proceed with other tests
    	try {
			log.info("clientId={}, clientSecret={}, redirectUri={}",
					inMemoryClients.getClientId(), inMemoryClients.getClientSecret(),
					URLEncoder.encode(inMemoryClients.getRedirectUri(), "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			log.error(e.getMessage(), e);
		}
    }
	
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
	public List<ServerAccessToken> getAccessTokens(Client client, UserSubject subject)
			throws OAuthServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<RefreshToken> getRefreshTokens(Client client, UserSubject subject)
			throws OAuthServiceException {
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
		
		Client client = null;
		
		if (inMemoryClients.getClientId().toString().equals(clientId)) {
			client = new Client();
			client.setAllowedGrantTypes(Arrays.<String>asList(
					OAuthConstants.AUTHORIZATION_CODE_GRANT,
					OAuthConstants.REFRESH_TOKEN
					));
			client.setClientId(inMemoryClients.getClientId().toString());
			client.setClientSecret(inMemoryClients.getClientSecret());
			client.setConfidential(true);
			client.setRedirectUris(Arrays.<String>asList(
					inMemoryClients.getRedirectUri()
					));
			client.setRegisteredScopes(Arrays.<String>asList(
					"demo1",
					"demo2"
					));
		}
		
		return client;
	}

	@Override
	protected void doRemoveClient(Client c) {
		// TODO Auto-generated method stub
		
	}

}
