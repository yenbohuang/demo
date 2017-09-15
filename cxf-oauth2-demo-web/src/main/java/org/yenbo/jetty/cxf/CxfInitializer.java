package org.yenbo.jetty.cxf;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.yenbo.jetty.thymeleaf.ThymeleafConfig;

/**
 * This class is automatically scanned by Spring.
 */
public class CxfInitializer implements WebApplicationInitializer {

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		
		AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
		context.register(new Class<?>[] {
			CxfConfiguration.class,
			Oauth2Configuration.class,
			BeanConfiguration.class,
			ThymeleafConfig.class
			});
		context.refresh();
		servletContext.addListener(new ContextLoaderListener(context));
		
		ServletRegistration.Dynamic cxfServlet =
				servletContext.addServlet("CXFServlet", CXFServlet.class);
		cxfServlet.setInitParameter("static-welcome-file", "/index.html");
		cxfServlet.setInitParameter("static-resources-list", "/index.html");
		cxfServlet.setLoadOnStartup(1);
		cxfServlet.addMapping("/*");
	}
}
