package org.yenbo.commonDemo;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpClientDemo {

	private static final Logger log = LoggerFactory.getLogger(HttpClientDemo.class);	
	
	public HttpClientDemo() {
	}

	private static final int LOG_OUTPUT_LIMIT = 20;

	public CloseableHttpResponse execute(HttpRequestBase httpRequestBase) {
		
		if (httpRequestBase == null) {
			throw new IllegalArgumentException("httpRequestBase is null");
		}
		
		// print debug logs
		log.info("{} {}", httpRequestBase.getMethod(), httpRequestBase.getURI().toString());
		
		if (httpRequestBase.getAllHeaders() != null) {
			for (Header header: httpRequestBase.getAllHeaders()) {
				log.info("Request header: " + header.toString());
			}
		}
		
		if (httpRequestBase instanceof HttpEntityEnclosingRequest) {
		
			HttpEntityEnclosingRequest httpEntityEnclosingRequest =
					(HttpEntityEnclosingRequest) httpRequestBase;
			
			if (httpEntityEnclosingRequest.getEntity() != null) {
				try {
					byte[] binary = EntityUtils.toByteArray(httpEntityEnclosingRequest.getEntity());
					
					if (binary.length < LOG_OUTPUT_LIMIT) {
						log.info("Request body as string: " + EntityUtils.toString(
								httpEntityEnclosingRequest.getEntity()));
						log.info("Request body as bytes: " + ArrayUtils.toString(binary));
					} else {
						log.info("Request body: sending {} bytes", binary.length);
					}
					
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
				
				if (binary.length < LOG_OUTPUT_LIMIT) {
					log.info("Response body as string: " + body);
					log.info("Response body as binary: " + ArrayUtils.toString(binary));
				} else {
					log.info("Response body: receiving {} bytes", binary.length);
				}
				
				return (CloseableHttpResponse) response;
			}
		};
		
		try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
			return httpClient.execute(httpRequestBase, responseHandler);
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
