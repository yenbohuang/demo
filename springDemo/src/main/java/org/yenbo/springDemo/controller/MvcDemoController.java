package org.yenbo.springDemo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class MvcDemoController {

	@RequestMapping(value="/mvcDemo", method=RequestMethod.GET)
	public String home() {
		// Use this URL for demo "http://localhost:8080/springDemo/mvcDemo/"
		return "home";
	}
}
