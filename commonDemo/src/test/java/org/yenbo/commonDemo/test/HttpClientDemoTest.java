package org.yenbo.commonDemo.test;

import java.net.URI;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yenbo.commonDemo.HttpClientDemo;

public class HttpClientDemoTest {

	private static final Logger log = LoggerFactory.getLogger(HttpClientDemoTest.class);
	
	public static void main(String[] args) {
		
		HttpClientDemo client = new HttpClientDemo();
		
		try {
		
			// http://localhost:8080/springDemo/restDemo?param=restDemo
			URI uri = new URIBuilder()
					.setScheme("http")
					.setHost("localhost")
					.setPort(8080)
					.setPath("/springDemo/restDemo")
					.addParameter("param", HttpClientDemoTest.class.getCanonicalName())
					.build();
			
			HttpGet httpGet = new HttpGet(uri);
			CloseableHttpResponse httpResponse = client.execute(httpGet, false);
			
			log.info(EntityUtils.toString(httpResponse.getEntity()));
			
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		}
	}
}
