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

public class InMemoryAuthorizationCodeDataProvider extends AbstractAuthorizationCodeDataProvider {

	private static final Logger log = LoggerFactory.getLogger(InMemoryAuthorizationCodeDataProvider.class);
	
	private static final long ACCESS_TOKEN_EXPIRED_TIME_SECONDS = Long.MAX_VALUE;

	@Autowired
	private ClientRepository clientRepository;
	
	@Autowired
	private AuthorizationCodeRepository authorizationCodeRepository;
	
	@Autowired
	private AccessTokenRepository accessTokenRepository;

	public InMemoryAuthorizationCodeDataProvider() {
		super();
    }
	
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
		BearerAccessToken token = new BearerAccessToken(client, UUID.randomUUID().toString(),
				ACCESS_TOKEN_EXPIRED_TIME_SECONDS, OAuthUtils.getIssuedAt());
		log.debug(
				"createNewAccessToken: responseType={}, refreshToken={}, tokenKey={}, tokenType={}",
				token.getResponseType(), token.getRefreshToken(), token.getTokenKey(),
				token.getTokenType());
		return token;
	}
	
	@Override
	protected boolean isRefreshTokenSupported(List<String> theScopes) {
		// make refresh token always supported
		log.debug("Always generate refresh token");
		return super.isRefreshTokenSupported(theScopes);
	}
	
	@Override
	public ServerAuthorizationCodeGrant createCodeGrant(AuthorizationCodeRegistration reg)
	        throws OAuthServiceException {
		
		ServerAuthorizationCodeGrant grant = super.createCodeGrant(reg);
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
		// TODO Auto-generated method stub
		log.warn("getAccessToken is not implemented");
		return null;
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
		accessTokenRepository.saveAccessToken(serverToken);
	}

	@Override
	protected void saveRefreshToken(RefreshToken refreshToken) {
		// TODO Auto-generated method stub
		log.warn("saveRefreshToken is not implemented");
	}

	@Override
	protected void doRevokeAccessToken(ServerAccessToken accessToken) {
		// TODO Auto-generated method stub
		log.warn("doRevokeAccessToken is not implemented");
	}

	@Override
	protected void doRevokeRefreshToken(RefreshToken refreshToken) {
		// TODO Auto-generated method stub
		log.warn("doRevokeRefreshToken is not implemented");
	}

	@Override
	protected RefreshToken getRefreshToken(String refreshTokenKey) {
		// TODO Auto-generated method stub
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
		
		return clientRepository.getClient(clietUuid);
	}

	@Override
	protected void doRemoveClient(Client c) {
		// TODO Auto-generated method stub
		log.warn("doRemoveClient is not implemented");
	}

}
