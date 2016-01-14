package org.yenbo.commonDemo;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpClientDemo {

	private static final Logger log = LoggerFactory.getLogger(HttpClientDemo.class);	
	
	public HttpClientDemo() {
	}
	
	public static void main(String[] args) {
		
		HttpClientDemo client = new HttpClientDemo();
		
		try {
		
			// http://localhost:8080/springDemo/restDemo?param=restDemo
			URI uri = new URIBuilder()
					.setScheme("http")
					.setHost("localhost")
					.setPort(8080)
					.setPath("/springDemo/restDemo")
					.addParameter("param", HttpClientDemo.class.getCanonicalName())
					.build();
			
			HttpGet httpGet = new HttpGet(uri);
			CloseableHttpResponse httpResponse = client.execute(httpGet, false);
			
			log.info(EntityUtils.toString(httpResponse.getEntity()));
			
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		}
	}

	private CloseableHttpClient createHttpCliept(boolean sslPeerUnverified) {
		
		if (sslPeerUnverified) {
		
			return HttpClientBuilder.create()
					.setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
					.build();
			
		} else {
			return HttpClients.createDefault();
		}
	}

	public CloseableHttpResponse execute(HttpRequestBase request, boolean sslPeerUnverified) {
		
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
			throw new CommonDemoException(ex);
		}
	}
	
	public void addHeader(HttpRequestBase httpRequestBase, String key, String value) {
		
		if (value != null) {
			httpRequestBase.addHeader(key, value);
		}
	}
	
	public void addEntity(HttpEntityEnclosingRequest httpEntityEnclosingRequest,
			String text) {
		
		if (text != null) {
			httpEntityEnclosingRequest.setEntity(EntityBuilder.create()
					.setContentEncoding("UTF-8")
					.setText(text)
					.build());
		}
	}
	
	public void addEntity(HttpEntityEnclosingRequest httpEntityEnclosingRequest,
			byte[] binary) {
		
		if (binary != null) {
			httpEntityEnclosingRequest.setEntity(EntityBuilder.create()
					.setBinary(binary)
					.build());
		}
	}
	
	public String encodeForUrl(String s) {
		
		try {
			return URLEncoder.encode(s, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new CommonDemoException(e);
		}
	}
}
