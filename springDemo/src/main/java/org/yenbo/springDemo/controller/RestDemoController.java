package org.yenbo.springDemo.controller;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.yenbo.commonDemo.response.DemoResponse;

@RestController
@RequestMapping("/restDemo")
public class RestDemoController {

	public RestDemoController() {
	}

	@RequestMapping(method=RequestMethod.GET)
	public DemoResponse getMethod(
			@RequestParam(value="param") String param) {
		
		// Use this URL for demo: "http://localhost:8080/springDemo/restDemo?param=restDemo"
		
		DemoResponse demoResponse = new DemoResponse();
		demoResponse.setClassName(getClass().getName());
		demoResponse.setMethodName("GET");
		demoResponse.setTime(ZonedDateTime.now(ZoneId.of("UTC")).toString());
		demoResponse.setParam(param);
		return demoResponse;
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public DemoResponse postMethod(
			@RequestParam(value="param") String param) {
		
		DemoResponse demoResponse = new DemoResponse();
		demoResponse.setClassName(getClass().getName());
		demoResponse.setMethodName("POST");
		demoResponse.setTime(ZonedDateTime.now(ZoneId.of("UTC")).toString());
		demoResponse.setParam(param);
		return demoResponse;
	}
}