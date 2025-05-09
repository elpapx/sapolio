package com.pichanga.application.config;

import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;
import com.amazonaws.services.secretsmanager.model.GetSecretValueRequest;
import com.amazonaws.services.secretsmanager.model.GetSecretValueResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pichanga.application.entity.PostgreDbCredentials;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class SecretsManagerConfig {
    private static final String DB_SECRET_NAME = "pichanga-secret-registration-db";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Bean
    public AWSSecretsManager awsSecretsManager() {
        return AWSSecretsManagerClientBuilder.defaultClient();
    }

    @Bean
    public PostgreDbCredentials getPostgreDbCredentials(AWSSecretsManager awsSecretsManager) throws IOException {
        GetSecretValueRequest getSecretValueRequest = new GetSecretValueRequest().withSecretId(DB_SECRET_NAME);
        GetSecretValueResult getSecretValueResult = awsSecretsManager.getSecretValue(getSecretValueRequest);

        try {
            return OBJECT_MAPPER.readValue(getSecretValueResult.getSecretString(), PostgreDbCredentials.class);
        } catch (Exception e) {
            throw new IOException("Cannot read value from AWS Secret Manager", e);
        }
    }
}
