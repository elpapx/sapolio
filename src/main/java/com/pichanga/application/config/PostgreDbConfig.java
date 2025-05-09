package com.pichanga.application.config;

import com.pichanga.application.entity.PostgreDbCredentials;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class PostgreDbConfig {
    private static final String DRIVER_CLASS_NAME = "org.postgresql.Driver";

    @Bean
    public DataSource dataSource(PostgreDbCredentials postgreDbCredentials) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(DRIVER_CLASS_NAME);
        dataSource.setUrl(postgreDbCredentials.getJdbcUrl());
        dataSource.setUsername(postgreDbCredentials.getUsername());
        dataSource.setPassword(postgreDbCredentials.getPassword());
        return dataSource;
    }
}
