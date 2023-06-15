package com.bh.drillingcommons.config;

import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;


/**
 * Created by michaelvelez on 5/13/16.
 * Configuration for spring security to provide authentication for endpoints
 */
@Configuration
@EnableResourceServer
public class ServiceSecurityConfig  extends ResourceServerConfigurerAdapter {
	
    /**
     * Provides Base Site Level Rules for User Authentication, such as: <br>
     * <ul>
     *  <li>Disabling CSRF : Required for PUT Requests to Work with Postman.</li>
     *  <li>Disabling Frame Options : Required for h2 Console to Work </li>
     *  <li>PermitAll OPTIONS Requests: OPTIONS requests are sent by browsers if
     *  the requests is considered non-standard, such as any request that has Authorization.</li>
     *  <li>Require Authentication For All Other Requests</li>
     * </ul>
     */
    @Override
	public void configure(HttpSecurity http) throws Exception {
		http.headers().frameOptions().disable();

		http.authorizeRequests().antMatchers(HttpMethod.OPTIONS).permitAll();

		http.csrf().requireCsrfProtectionMatcher(new RequestMatcher() {
			private Pattern allowedMethods = Pattern.compile("^(GET|HEAD|TRACE|OPTIONS|POST|PUT|DELETE)$");

			@Override
			public boolean matches(HttpServletRequest request) {
				return !allowedMethods.matcher(request.getMethod()).matches();
			}
		}).and().authorizeRequests()
				.antMatchers("/swagger-ui.html", "/v2/api-docs", "/swagger-resources/**", "/configuration/security",
						"/webjars/**", "/documentation/swagger", "/api/v1/userprofile/requestaccess/form", "/api/v1/userprofile/troubleSigning/help")
				.permitAll().anyRequest().authenticated();

	}
	@Bean
	@Primary
	public ResourceServerTokenServices tokenService() {
		CustomRemoteTokenService tokenServices = new CustomRemoteTokenService();
		return tokenServices;
	}

    /*@Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.eraseCredentials(true)
            .inMemoryAuthentication()
            .withUser("idm").password("{bcrypt}$2a$10$O1W/nRzF/LJXArbaVIuA5uyisR9HEcsL7Lv/q.kxWLrAcK06we2dm").roles("IDM").and()
            .withUser("engagedrillinguser").password("{bcrypt}$2a$10$i15N/6vzAUp7cHZFoe4rgO1X6ytkwk6YtLplbaKO7On6D3FRzLjEC").roles("USER");
    }
*/
}
