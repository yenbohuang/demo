package org.yenbo.commonUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyHttpClientUtils {

	private static final Logger log = LoggerFactory.getLogger(MyHttpClientUtils.class);
	
	private static CloseableHttpClient createHttpCliept(boolean sslPeerUnverified) {
		
		if (sslPeerUnverified) {
		
			return HttpClientBuilder.create()
					.setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
					.build();
			
		} else {
			return HttpClients.createDefault();
		}
	}

	public static CloseableHttpResponse execute(HttpRequestBase request, boolean sslPeerUnverified) {
		
		if (request == null) {
			throw new IllegalArgumentException("request is null");
		}
		
		// print debug logs
		log.info("{} {}", request.getMethod(), request.getURI().toString());
		
		if (request.getAllHeaders() != null) {
			for (Header header: request.getAllHeaders()) {
				log.info("Request header: " + header.toString());
			}
		}
		
		if (request instanceof HttpEntityEnclosingRequest) {
		
			HttpEntityEnclosingRequest httpEntityEnclosingRequest =
					(HttpEntityEnclosingRequest) request;
			
			if (httpEntityEnclosingRequest.getEntity() != null) {
				try {
					log.info("Request body as string: {}",
							EntityUtils.toString(httpEntityEnclosingRequest.getEntity()));
					log.info("Request body as bytes: {}",
							ArrayUtils.toString(EntityUtils.toByteArray(
									httpEntityEnclosingRequest.getEntity())));
				} catch (IOException ex) {
					log.error(ex.getMessage(), ex);
				}
			}
		}
		
		// send request
		ResponseHandler<CloseableHttpResponse> responseHandler =
				new ResponseHandler<CloseableHttpResponse>() {

			@Override
			public CloseableHttpResponse handleResponse(HttpResponse response)
					throws ClientProtocolException, IOException {
				
				String body = null;
				byte[] binary = null;
				
				if (response.getEntity() != null) {
					response.setEntity(new BufferedHttpEntity(response.getEntity()));
					body = EntityUtils.toString(response.getEntity());
					binary = EntityUtils.toByteArray(response.getEntity());
				}
				
				log.info("Response status line: " + response.getStatusLine());

				if (response.getAllHeaders() != null) {
					
					for (Header header: response.getAllHeaders()) {
						log.info("Response header: " + header.toString());
					}
				}
				
				if (binary == null) {
					log.info("Response body: null");
				} else {
					log.info("Response body as string: " + body);
					log.info("Response body as binary: " + ArrayUtils.toString(binary));
				}
				
				return (CloseableHttpResponse) response;
			}
		};
		
		try (CloseableHttpClient httpClient = createHttpCliept(sslPeerUnverified)) {
			return httpClient.execute(request, responseHandler);
		} catch (IOException ex) {
			throw new CommonUtilsException(ex);
		}
	}
	
	public static void addHeader(HttpRequestBase httpRequestBase, String key, String value) {
		
		if (httpRequestBase == null) {
			throw new IllegalArgumentException("httpRequestBase is null");
		}
		
		httpRequestBase.addHeader(key, value);
	}
	
	public static void addEntity(HttpEntityEnclosingRequest httpEntityEnclosingRequest,
			String text) {
		
		if (httpEntityEnclosingRequest == null) {
			throw new IllegalArgumentException("httpEntityEnclosingRequest is null");
		}
		
		httpEntityEnclosingRequest.setEntity(EntityBuilder.create()
				.setContentEncoding("UTF-8")
				.setText(text)
				.build());
	}
	
	public static void addEntity(HttpEntityEnclosingRequest httpEntityEnclosingRequest,
			byte[] binary) {
		
		if (httpEntityEnclosingRequest == null) {
			throw new IllegalArgumentException("httpEntityEnclosingRequest is null");
		}
		
		httpEntityEnclosingRequest.setEntity(EntityBuilder.create()
				.setBinary(binary)
				.build());
	}
	
	public static String encodeForUrl(String s) {
		
		if (StringUtils.isBlank(s)) {
			throw new IllegalArgumentException("s is blank");
		}
		
		try {
			return URLEncoder.encode(s, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new CommonUtilsException(e);
		}
	}
}
