package org.yenbo.jetty.config.cxf;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * This class is automatically scanned by Spring.
 */
public class CxfInitializer implements WebApplicationInitializer {

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		
		AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
		rootContext.register(new Class<?>[] {CxfConfiguration.class});
		servletContext.addListener(new ContextLoaderListener(rootContext));
		
		AnnotationConfigWebApplicationContext webContext = new AnnotationConfigWebApplicationContext();
		webContext.setParent(rootContext);
		
		// TODO Spring MVC does not work yet
		// No mapping found for HTTP request with URI [/forms/home] in DispatcherServlet with name 'DispatcherServlet'
		ServletRegistration.Dynamic springDispatcher =
				servletContext.addServlet("DispatcherServlet", new DispatcherServlet(webContext));
		springDispatcher.setLoadOnStartup(1);
		springDispatcher.addMapping("/forms/*");

		// CXF works ONLY when it acts as the default servlet handler
	    ServletRegistration.Dynamic cxfDispatcher =
	    		servletContext.addServlet("CXFServlet", CXFServlet.class);
	    cxfDispatcher.setLoadOnStartup(1);
	    cxfDispatcher.addMapping("/*");
	}
}
