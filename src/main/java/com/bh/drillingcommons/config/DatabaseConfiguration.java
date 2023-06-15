package com.bh.drillingcommons.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.bh.drillingcommons.util.EncryptDecryptUtil;

@Configuration
@EnableTransactionManagement
@PropertySource(value = { "classpath:application.properties" })
public class DatabaseConfiguration {
	
	@Autowired
	private Environment env;

	@Bean
	public DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("oracle.jdbc.driver.OracleDriver");
		dataSource.setUsername(env.getProperty("spring.datasource.username"));
		dataSource.setPassword(EncryptDecryptUtil.decrypt(env.getProperty("spring.datasource.password")));
		dataSource.setUrl(env.getProperty("spring.datasource.jdbc-url"));
		return dataSource;
	}

	@Bean
	@Primary
	LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder) {
		return builder.dataSource(dataSource()).packages("com.bh.drillingcommons.entity.oracle")
				.persistenceUnit("oracle").build();
	}

	@Bean(name = {"oracleTransactionManager", "transactionManager"})
	public PlatformTransactionManager oracleTransactionManager(
			final LocalContainerEntityManagerFactoryBean entityManager) {
		return new JpaTransactionManager(entityManager.getObject());
	}
}
