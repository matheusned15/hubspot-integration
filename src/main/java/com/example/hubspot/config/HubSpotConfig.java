package com.example.hubspot.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "hubspot")
@Data
public class HubSpotConfig {
    private String clientId;
    private String clientSecret;
    private String redirectUri;
}
