package org.yenbo.tomcatDemo.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yenbo.commonDemo.response.DemoResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@WebServlet("/servletByAnnotation")
public class ServletByAnnotation extends HttpServlet {

	private static final long serialVersionUID = -4199974658388696243L;

	private static final Logger log = LoggerFactory.getLogger(ServletByAnnotation.class);
	
	public ServletByAnnotation() {
		super();
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
 
		// Use this URL for demo: "http://localhost:8080/tomcatDemo/servletByAnnotation"
		DemoResponse demoResponse = new DemoResponse();
		demoResponse.setClassName(getClass().getName());
		demoResponse.setMethodName(request.getMethod());
		demoResponse.setTime(ZonedDateTime.now(ZoneId.of("UTC")).toString());
		
		String json = new Gson().toJson(demoResponse);
		log.info(json);
		
        PrintWriter writer = response.getWriter();
        writer.println(json);
        writer.flush();
    }
 
	@Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
		
		DemoResponse demoResponse = new DemoResponse();
		demoResponse.setClassName(getClass().getName());
		demoResponse.setMethodName(request.getMethod());
		demoResponse.setTime(ZonedDateTime.now(ZoneId.of("UTC")).toString());
		demoResponse.setParam(request.getParameter("param"));
		
        Gson gson = new GsonBuilder().serializeNulls().create();
		String json = gson.toJson(demoResponse);
		log.info(json);
		
        PrintWriter writer = response.getWriter();
        writer.println(json);
        writer.flush();
    }
}
