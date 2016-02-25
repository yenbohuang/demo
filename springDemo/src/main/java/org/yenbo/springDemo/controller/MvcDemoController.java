package org.yenbo.springDemo.controller;

import java.time.ZonedDateTime;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class MvcDemoController {

	@RequestMapping(value="/mvcDemo", method=RequestMethod.GET)
	public String home(Model model) {
		// Use this URL for demo "http://localhost:8080/springDemo/mvcDemo/"
		
		model.addAttribute("serverTime", ZonedDateTime.now());
		return "home";
	}
}
