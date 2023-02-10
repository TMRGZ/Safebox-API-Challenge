package com.rviewer.skeletons.infrastructure.config.endpoint;

import com.rviewer.skeletons.infrastructure.rest.safebox.service.invoker.ApiClient;
import com.rviewer.skeletons.infrastructure.rest.safebox.service.SafeboxServiceApi;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "endpoints.safebox-service")
public class ServiceEndpointConfig {

    @NotNull
    private String url;

    @Bean
    public SafeboxServiceApi safeboxServiceApi(ApiClient apiClient) {
        apiClient.setBasePath(url);
        return new SafeboxServiceApi(apiClient);
    }
}
