package org.yenbo.jetty.config.cxf;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

/**
 * This class is automatically scanned by Spring.
 */
public class CxfInitializer implements WebApplicationInitializer {

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		
		AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
		context.register(new Class<?>[] {CxfConfiguration.class});
		context.refresh();
		servletContext.addListener(new ContextLoaderListener(context));
	    servletContext.addServlet("CXFServlet", CXFServlet.class).addMapping("/*");
	}

}
