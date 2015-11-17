package org.yenbo.tomcatDemo.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yenbo.tomcatDemo.response.User;

import com.google.gson.Gson;

@WebServlet("/jndiDemo")
public class JndiDemo extends HttpServlet {

	private static final long serialVersionUID = 4231576965867843705L;

	private static final Logger log = LoggerFactory.getLogger(JndiDemo.class);
	
	public JndiDemo() {
		super();
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
		
		// Use this URL for demo: "http://localhost:8080/tomcatDemo/jndiDemo"
		List<User> users = new ArrayList<>();
		
		try {
			Context context = new InitialContext();
			DataSource ds = (DataSource)context.lookup("java:/comp/env/jdbc/demo");
			
			try (Connection conn = ds.getConnection();
					PreparedStatement statement = conn.prepareStatement("select * from users");
					ResultSet rs = statement.executeQuery()) {
				
				while (rs.next()) {
					User user = new User();
					user.setId(rs.getInt("id"));
					user.setName(rs.getString("name"));
					user.setQueryTime(ZonedDateTime.now(ZoneId.of("UTC")).toString());
					users.add(user);
				}
				
			} finally {
			}
			
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		}
		
		PrintWriter writer = response.getWriter();
        writer.println(new Gson().toJson(users));
        writer.flush();
	}
}
