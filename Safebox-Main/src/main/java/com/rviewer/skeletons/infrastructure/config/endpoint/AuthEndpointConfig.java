package com.rviewer.skeletons.infrastructure.config.endpoint;

import com.rviewer.skeletons.infrastructure.rest.safebox.auth.LoginApi;
import com.rviewer.skeletons.infrastructure.rest.safebox.auth.UserApi;
import com.rviewer.skeletons.infrastructure.rest.safebox.auth.invoker.ApiClient;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "endpoints.safebox-auth")
public class AuthEndpointConfig {

    @NotNull
    private String url;

    @Bean
    public LoginApi loginApi(ApiClient apiClient) {
        apiClient.setBasePath(url);
        return new LoginApi(apiClient);
    }

    @Bean
    public UserApi userApi(ApiClient apiClient) {
        apiClient.setBasePath(url);
        return new UserApi(apiClient);
    }


}
