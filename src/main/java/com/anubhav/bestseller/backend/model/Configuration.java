package com.anubhav.bestseller.backend.model;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "app")
@Component
public class Configuration {
    private SessionData mockUser;

    private String environment;

    public SessionData getMockUser() {
        return mockUser;
    }

    public void setMockUser(SessionData mockUser) {
        this.mockUser = mockUser;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }
}

