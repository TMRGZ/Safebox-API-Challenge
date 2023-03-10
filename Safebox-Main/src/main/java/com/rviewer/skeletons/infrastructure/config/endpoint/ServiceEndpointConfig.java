package com.rviewer.skeletons.infrastructure.config.endpoint;

import com.rviewer.skeletons.infrastructure.rest.safebox.holder.SafeboxHolderApi;
import com.rviewer.skeletons.infrastructure.rest.safebox.holder.invoker.ApiClient;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "endpoints.safebox-service")
public class ServiceEndpointConfig {

    @NotNull
    private String url;

    @Bean
    public SafeboxHolderApi safeboxHolderApi(ApiClient apiClient) {
        apiClient.setBasePath(url);
        return new SafeboxHolderApi(apiClient);
    }
}
