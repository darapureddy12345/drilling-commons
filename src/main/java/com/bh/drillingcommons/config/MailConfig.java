package com.bh.drillingcommons.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.bh.drillingcommons.config.MailConfig;

/**
 * {@link MailConfig} 
 * 
 * @author BH
 * 
 */
@Component
public class MailConfig {

	@Value("${request.access.email.from}")
	private String emailFrom;
	
	@Value("${app.shutdown.email.to}")
	private String emailTo;

	@Value("${app.shutdown.email.cc}")
	private String emailCc;

	@Value("${environment:dev}")
	private String environment;
	
	@Value("${app.name:microservice}")
	private String appName;
	
	public String getEmailFrom() {
		return emailFrom;
	}

	public String getEmailTo() {
		return emailTo;
	}
	
	public String getEmailCc() {
		return emailCc;
	}
	
	public String getEnvironment() {
		return environment;
	}
	
	public String getAppName() {
		return appName;
	}
}
