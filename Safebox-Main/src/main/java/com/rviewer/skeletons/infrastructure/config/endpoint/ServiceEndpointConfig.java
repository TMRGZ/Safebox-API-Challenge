package com.rviewer.skeletons.infrastructure.config.endpoint;

import com.rviewer.skeletons.infrastructure.rest.safebox.holder.invoker.ApiClient;
import com.rviewer.skeletons.infrastructure.rest.safebox.holder.SafeboxHolderApi;
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
    public SafeboxHolderApi safeboxHolderApi(ApiClient apiClient) {
        apiClient.setBasePath(url);
        return new SafeboxHolderApi(apiClient);
    }
}
