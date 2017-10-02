package org.yenbo.jetty.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityBeansConfig extends WebSecurityConfigurerAdapter {

	private static final String LOGIN_FORM_URL = "/oauth2/login";
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		// TODO the hidden "_csrf" field is not added automatically for "th:action"
		http.csrf().disable()
			.exceptionHandling()
				.authenticationEntryPoint(new DemoLoginUrlAuthenticationEntryPoint(LOGIN_FORM_URL))
			.and().formLogin().loginPage(LOGIN_FORM_URL).permitAll()
			.and().logout()
				.logoutUrl("/oauth2/logout")
			.and().authorizeRequests()
				.regexMatchers(
						LOGIN_FORM_URL + ".*",
						"/api/.*",
						"/index.html$",
						"/static/.*"
					).permitAll()
				.regexMatchers(
						// Prevent user from knowing access token and refresh token.
						"/oauth2/token$",
						"/oauth2resx/resx"
						).anonymous()
				.anyRequest().authenticated();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		
		auth.userDetailsService(new DemoUserDetailsService())
			.passwordEncoder(new StupidPasswordEncoder());
	}	
}
