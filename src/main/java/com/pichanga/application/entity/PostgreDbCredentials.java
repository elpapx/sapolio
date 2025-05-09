package com.pichanga.application.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PostgreDbCredentials {
    private String username;
    private String password;
    @JsonProperty("dbname")
    private String dbName;
    private String engine;
    private String port;
    private String host;
    private String dbInstanceIdentifier;

    public String getJdbcUrl() {
        return String.format("jdbc:postgresql://%s:%s/%s", host, port, dbName);
    }
}

