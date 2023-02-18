package com.rviewer.skeletons.infrastructure.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "safebox-service-messaging-config")
public class SafeboxHolderMessagingConfig {

    @NotNull
    private String queue;

    @NotNull
    private String exchange;

    @NotNull
    private String routingKey;

}
