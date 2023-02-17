package com.rviewer.skeletons.infrastructure.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "appconfig")
public class AppConfig {

    @NotNull
    private Integer maxTries;

    @NotNull
    private String tokenSecret;

    @NotNull
    private Integer tokenExpirationMinutes;

}
