package org.yenbo.jetty.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, jsr250Enabled = true)
public class SecurityBeansConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.antMatchers("/api/**").anonymous() // TODO why this is blocked after login?
			.anyRequest().authenticated()
			.and().formLogin(); // TODO add customized login form
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// replace this with a real database
		auth.inMemoryAuthentication()
			.withUser("user").password("password").roles("USER")
			.and()
			.withUser("admin").password("password").roles("USER", "ADMIN");
	}	
}
