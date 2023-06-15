package com.bh.drillingcommons.config;

import static com.google.common.base.Predicates.alwaysFalse;
import static springfox.documentation.builders.PathSelectors.regex;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;

import com.google.common.base.Predicate;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {

    @Autowired
    Environment env;    
    
    @Bean
    @Profile(value={"dev","qa"})
    public Docket userApiDocketDev() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(coreApiInfo())
                .directModelSubstitute(LocalDateTime.class, java.util.Date.class)
                .select()
                .paths(apiPaths())
                .build()
                .directModelSubstitute(LocalDate.class, String.class)
                .genericModelSubstitutes(ResponseEntity.class)
                .useDefaultResponseMessages(false);
    }
    
    @Bean
    @Profile(value={"default","prod"})
    public Docket userApiDocketOther() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(coreApiInfo())
                .directModelSubstitute(LocalDateTime.class, java.util.Date.class)
                .select()
                .paths(alwaysFalse())
                .build();
    }
    
    private ApiInfo coreApiInfo() {
        String termsOfServiceUrl = "";
        String license = "";
        String licenseUrl = "";
        
        Contact contact = new Contact("GEOG Engage Drilling Team","",env.getProperty("info.support.distributionList"));
        
        return new ApiInfoBuilder()
            .title(env.getProperty("info.app.name"))
            .description(env.getProperty("info.app.description"))
            .termsOfServiceUrl(termsOfServiceUrl)
            .contact(contact)
            .license(license)
            .licenseUrl(licenseUrl)
            .version(env.getProperty("info.build.version"))
            .build();
    }

    private Predicate<String> apiPaths() {
        return regex("/api.*");
    }
}