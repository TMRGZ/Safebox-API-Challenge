package com.rviewer.skeletons.infrastructure.config;

import com.rviewer.skeletons.infrastructure.rest.safebox.ApiClient;
import com.rviewer.skeletons.infrastructure.rest.safebox.auth.LoginApi;
import com.rviewer.skeletons.infrastructure.rest.safebox.auth.UserApi;
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
@ConfigurationProperties(prefix = "endpoints")
public class EndpointsConfig {

    @NotNull
    private String safeboxAuth;

    @NotNull
    private String safeboxService;

    @Bean
    public LoginApi loginApi(ApiClient apiClient) {
        apiClient.setBasePath(safeboxAuth);
        return new LoginApi(apiClient);
    }

    @Bean
    public UserApi userApi(ApiClient apiClient) {
        apiClient.setBasePath(safeboxAuth);
        return new UserApi(apiClient);
    }

    @Bean
    public SafeboxServiceApi safeboxServiceApi(ApiClient apiClient) {
        apiClient.setBasePath(safeboxService);
        return new SafeboxServiceApi(apiClient);
    }
}
