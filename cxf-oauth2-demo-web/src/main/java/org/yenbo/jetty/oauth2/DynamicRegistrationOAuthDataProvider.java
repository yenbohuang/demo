package org.yenbo.jetty.oauth2;

import java.util.List;
import java.util.UUID;

import org.apache.cxf.rs.security.oauth2.common.AuthenticationMethod;
import org.apache.cxf.rs.security.oauth2.common.Client;
import org.apache.cxf.rs.security.oauth2.common.ServerAccessToken;
import org.apache.cxf.rs.security.oauth2.common.UserSubject;
import org.apache.cxf.rs.security.oauth2.provider.AbstractOAuthDataProvider;
import org.apache.cxf.rs.security.oauth2.provider.OAuthServiceException;
import org.apache.cxf.rs.security.oauth2.tokens.bearer.BearerAccessToken;
import org.apache.cxf.rs.security.oauth2.tokens.refresh.RefreshToken;
import org.apache.cxf.rs.security.oauth2.utils.OAuthUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.yenbo.jetty.data.InMemoryClient;
import org.yenbo.jetty.data.InMemoryUser;
import org.yenbo.jetty.repo.ClientRepository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DynamicRegistrationOAuthDataProvider extends AbstractOAuthDataProvider {

	private static final Logger log = LoggerFactory.getLogger(
			DynamicRegistrationOAuthDataProvider.class);
	
	private static final String MANAGEMENT_CLIENT_ID = "6a73a7ea-676a-47ae-99c3-ce9fb41b3972";
	
	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
	
	@Autowired
	private ClientRepository clientRepository;
	
	public DynamicRegistrationOAuthDataProvider() {
		super();
	}

	private Client getManagementClient() {
		Client client = new Client();
		client.setClientId(MANAGEMENT_CLIENT_ID);
		client.setConfidential(true);
		return client;
	}
	
	private UserSubject getManagementUserSubject() {
		UserSubject subject = new UserSubject();
		subject.setAuthenticationMethod(AuthenticationMethod.PASSWORD);
		subject.setLogin(InMemoryUser.USERNAME_ADMIN);
		subject.getRoles().add(InMemoryUser.ROLE_ADMIN);
		return subject;
	}
	
	@Override
	public ServerAccessToken getAccessToken(String accessToken) throws OAuthServiceException {
		
		// Assume that access token is not issued by this demo but from another internal system.
		
		BearerAccessToken serverAccessToken = null;
		
		if (InMemoryUser.ACCESS_TOKEN_ADMIN.equals(accessToken)) {
			
			serverAccessToken = new BearerAccessToken();
			serverAccessToken.setClient(getManagementClient());
			serverAccessToken.setExpiresIn(Oauth2Factory.ACCESS_TOKEN_EXPIRED_TIME_SECONDS);
			
			// always valid for demo
			serverAccessToken.setIssuedAt(OAuthUtils.getIssuedAt()); 
			
			serverAccessToken.setSubject(getManagementUserSubject());
			serverAccessToken.setTokenKey(accessToken);
		}
		
		return serverAccessToken;
	}

	@Override
	public List<ServerAccessToken> getAccessTokens(Client client, UserSubject subject)
			throws OAuthServiceException {
		log.warn("getAccessTokens is not implemented");
		return null;
	}

	@Override
	public List<RefreshToken> getRefreshTokens(Client client, UserSubject subject)
			throws OAuthServiceException {
		log.warn("getRefreshTokens is not implemented");
		return null;
	}

	@Override
	public void setClient(Client client) {
		// TODO Auto-generated method stub
		log.warn("setClient is not implemented");
	}

	@Override
	public List<Client> getClients(UserSubject resourceOwner) {
		log.warn("getClients is not implemented");
		return null;
	}

	@Override
	protected void saveAccessToken(ServerAccessToken serverToken) {
		log.warn("saveAccessToken is not implemented");
	}

	@Override
	protected void saveRefreshToken(RefreshToken refreshToken) {
		log.warn("saveRefreshToken is not implemented");
	}

	@Override
	protected void doRevokeAccessToken(ServerAccessToken accessToken) {
		log.warn("doRevokeAccessToken is not implemented");
	}

	@Override
	protected void doRevokeRefreshToken(RefreshToken refreshToken) {
		log.warn("doRevokeRefreshToken is not implemented");
	}

	@Override
	protected RefreshToken getRefreshToken(String refreshTokenKey) {
		log.warn("getRefreshToken is not implemented");
		return null;
	}

	@Override
	protected Client doGetClient(String clientId) {
		
		UUID clietUuid = null;
		
		try {
			clietUuid = UUID.fromString(clientId);
		} catch (IllegalArgumentException ex) {
			log.error(ex.getMessage(), ex);
			return null;
		}
		
		Client client = null;
		InMemoryClient inMemoryClient = clientRepository.get(clietUuid);
		
		if (null != inMemoryClient) {
			client = Oauth2Factory.create(inMemoryClient);
			
			if (log.isDebugEnabled()) {
				try {
					log.debug("InMemoryClient={}",
							OBJECT_MAPPER.writeValueAsString(inMemoryClient));
					log.debug("Client={}",
							OBJECT_MAPPER.writeValueAsString(client));
				} catch (JsonProcessingException ex) {
					log.error(ex.getMessage(), ex);
				}
			}
		}
		
		return client;
	}

	@Override
	protected void doRemoveClient(Client c) {
		// TODO
		log.warn("doRemoveClient is not implemented");
	}
}
