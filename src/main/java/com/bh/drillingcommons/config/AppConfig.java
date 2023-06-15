package com.bh.drillingcommons.config;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.bh.drillingcommons.enumerators.Service;

@SuppressWarnings("deprecation")
public class AppConfig {

	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(AppConfig.class);

	@Autowired
	Environment env;

	@Bean
	public Map<String, String> services() {
		Map<String, String> services = new HashMap<>();

		for (Service.GE geService : Service.GE.values()) {
			services.put(geService.getAppKey(), env.getProperty(geService.getAppKey()));
		}

		return services;
	}

	@Bean
	@Profile(value = { "local", "dev", "qa" })
	public WebMvcConfigurer setupForDev() {
		// return corsConfiguration
		return new WebMvcConfigurerAdapter() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/api/**").allowedOrigins("https://localhost:8085","https://dev-engagedrilling.azure.bakerhughes.com","https://qa.engagedrilling.com","https://qa-public.engagedrilling.com").allowedMethods("GET", "POST", "PUT", "DELETE");
			}
		};
	}

	@Bean
	@Profile(value = { "prod", "default" })
	public WebMvcConfigurer setupForProd() {
		return new WebMvcConfigurerAdapter() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/api/**").allowedOrigins("https://app.engagedrilling.com","https://www.engagedrilling.com").allowedMethods("GET",
						"POST", "PUT", "DELETE");
			}
		};
	}
}
