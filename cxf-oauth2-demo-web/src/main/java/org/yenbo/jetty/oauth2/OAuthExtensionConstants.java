package org.yenbo.jetty.oauth2;

import org.apache.cxf.rs.security.oauth2.services.ClientRegistration;

public class OAuthExtensionConstants {

	public static final String INVALID_CLIENT_METADATA = "invalid_client_metadata";
	public static final String INVALID_REDIRECT_URI = "invalid_redirect_uri";
	public static final String SOFTWARE_ID = "software_id";
	
	// The followings are only for demo, not from any RFCs.
	public static final String CLIENT_NAME_PREFIX = ClientRegistration.CLIENT_NAME + "#";
	public static final String CLIENT_DESCRIPTION = "client_description";
	
	private OAuthExtensionConstants() {}
}
