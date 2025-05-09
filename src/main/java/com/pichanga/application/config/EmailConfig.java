package com.pichanga.application.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.services.ses.SesClient;

@Configuration
public class EmailConfig {

    @Bean
    public SesClient sesClient() {
        return SesClient.builder()
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }
}
