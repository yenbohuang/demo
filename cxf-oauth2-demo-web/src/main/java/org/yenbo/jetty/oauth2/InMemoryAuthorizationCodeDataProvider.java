package org.yenbo.jetty.oauth2;

import java.util.List;
import java.util.UUID;

import org.apache.cxf.rs.security.oauth2.common.Client;
import org.apache.cxf.rs.security.oauth2.common.OAuthPermission;
import org.apache.cxf.rs.security.oauth2.common.ServerAccessToken;
import org.apache.cxf.rs.security.oauth2.common.UserSubject;
import org.apache.cxf.rs.security.oauth2.grants.code.AbstractAuthorizationCodeDataProvider;
import org.apache.cxf.rs.security.oauth2.grants.code.AuthorizationCodeRegistration;
import org.apache.cxf.rs.security.oauth2.grants.code.ServerAuthorizationCodeGrant;
import org.apache.cxf.rs.security.oauth2.provider.OAuthServiceException;
import org.apache.cxf.rs.security.oauth2.tokens.bearer.BearerAccessToken;
import org.apache.cxf.rs.security.oauth2.tokens.refresh.RefreshToken;
import org.apache.cxf.rs.security.oauth2.utils.OAuthUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class InMemoryAuthorizationCodeDataProvider extends AbstractAuthorizationCodeDataProvider {

	private static final Logger log = LoggerFactory.getLogger(InMemoryAuthorizationCodeDataProvider.class);
	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
	
	private static final long ACCESS_TOKEN_EXPIRED_TIME_SECONDS = 12345L;
	private static final long REFRESH_TOKEN_EXPIRED_TIME_SECONDS = 67890L;
	

	@Autowired
	private ClientRepository clientRepository;
	
	@Autowired
	private AuthorizationCodeRepository authorizationCodeRepository;
	
	@Autowired
	private TokenRepository tokenRepository;
	
	@Override
	protected void convertSingleScopeToPermission(Client client, String scope, List<OAuthPermission> perms) {
		
		// skip preset permissionMap and compose allowed list from client definition
		if (client.getRegisteredScopes().contains(scope)) {
			perms.add(new OAuthPermission(scope));
		} else {
			String msg = String.format(
					"Unexpected scope: clientId=%s, scope=%s, registeredScopes=%s",
					client.getClientId(), scope, client.getRegisteredScopes());
			log.warn(msg);
			throw new OAuthServiceException(msg);
		}
	}
	
	@Override
	protected ServerAccessToken createNewAccessToken(Client client, UserSubject userSub) {
		
		// generate customized access token
		BearerAccessToken accessToken = new BearerAccessToken(client,
				"AT:" + UUID.randomUUID().toString(),
				ACCESS_TOKEN_EXPIRED_TIME_SECONDS, OAuthUtils.getIssuedAt());
		try {
			log.debug("{}", OBJECT_MAPPER.writeValueAsString(accessToken));
		} catch (JsonProcessingException e) {
			log.error(e.getMessage(), e);
		}
		
		return accessToken;
	}
	
	@Override
	protected boolean isRefreshTokenSupported(List<String> theScopes) {
		
		// make refresh token always supported
		log.debug("Always generate refresh token");
		return true;
	}
	
	@Override
	protected RefreshToken doCreateNewRefreshToken(ServerAccessToken at) {
		
		// generate customized refresh token
		RefreshToken refreshToken = super.doCreateNewRefreshToken(at);
		refreshToken.setTokenKey("RT:" + UUID.randomUUID().toString());
		refreshToken.setExpiresIn(REFRESH_TOKEN_EXPIRED_TIME_SECONDS);
		
		try {
			log.debug("{}", OBJECT_MAPPER.writeValueAsString(refreshToken));
		} catch (JsonProcessingException e) {
			log.error(e.getMessage(), e);
		}
		
		return refreshToken;
	}
	
	@Override
	protected ServerAccessToken doRefreshAccessToken(Client client, RefreshToken oldRefreshToken,
			List<String> restrictedScopes) {
		
		// always renew and grant the same scopes from old refresh token
		restrictedScopes.clear();
		ServerAccessToken accessToken = super.doRefreshAccessToken(
				client, oldRefreshToken, restrictedScopes);
		
		try {
			log.debug("{}", OBJECT_MAPPER.writeValueAsString(accessToken));
		} catch (JsonProcessingException e) {
			log.error(e.getMessage(), e);
		}
		
		return accessToken;
	}
	
	@Override
	public ServerAuthorizationCodeGrant createCodeGrant(AuthorizationCodeRegistration reg)
	        throws OAuthServiceException {
		
		ServerAuthorizationCodeGrant grant = super.createCodeGrant(reg);
		
		try {
			log.debug("{}", OBJECT_MAPPER.writeValueAsString(grant));
		} catch (JsonProcessingException e) {
			log.error(e.getMessage(), e);
		}
		
		authorizationCodeRepository.saveAuthorizationCode(grant);
		return grant;
    }
	
	@Override
	public ServerAuthorizationCodeGrant removeCodeGrant(String code) throws OAuthServiceException {
		
		ServerAuthorizationCodeGrant grant = authorizationCodeRepository.getAuthorizationCode(code);
		
		if (null != grant) {
			authorizationCodeRepository.deleteAuthorizationCode(grant);
		}
		
		return grant;
	}

	@Override
	public List<ServerAuthorizationCodeGrant> getCodeGrants(Client c, UserSubject subject)
			throws OAuthServiceException {
		// TODO Auto-generated method stub
		log.warn("getCodeGrants is not implemented");
		return null;
	}

	@Override
	public ServerAccessToken getAccessToken(String accessToken) throws OAuthServiceException {
		return tokenRepository.getAccessToken(accessToken);
	}

	@Override
	public List<ServerAccessToken> getAccessTokens(Client client, UserSubject subject)
			throws OAuthServiceException {
		// TODO Auto-generated method stub
		log.warn("getAccessTokens is not implemented");
		return null;
	}

	@Override
	public List<RefreshToken> getRefreshTokens(Client client, UserSubject subject)
			throws OAuthServiceException {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		log.warn("getClients is not implemented");
		return null;
	}

	@Override
	protected void saveAccessToken(ServerAccessToken serverToken) {
		tokenRepository.saveAccessToken(serverToken);
	}

	@Override
	protected void saveRefreshToken(RefreshToken refreshToken) {
		tokenRepository.saveRefreshToken(refreshToken);
	}

	@Override
	protected void doRevokeAccessToken(ServerAccessToken accessToken) {
		tokenRepository.deleteAccessToken(accessToken);
	}

	@Override
	protected void doRevokeRefreshToken(RefreshToken refreshToken) {
		tokenRepository.deleteRefreshToken(refreshToken);
	}

	@Override
	protected RefreshToken getRefreshToken(String refreshTokenKey) {
		return tokenRepository.getRefreshToken(refreshTokenKey);
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
		
		return clientRepository.getClient(clietUuid);
	}

	@Override
	protected void doRemoveClient(Client c) {
		// TODO Auto-generated method stub
		log.warn("doRemoveClient is not implemented");
	}

}
