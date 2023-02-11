package com.rviewer.skeletons.infrastructure.config;

import com.rviewer.skeletons.domain.service.SafeboxService;
import com.rviewer.skeletons.domain.service.SafeboxServiceService;
import com.rviewer.skeletons.domain.service.TokenService;
import com.rviewer.skeletons.domain.service.UserService;
import com.rviewer.skeletons.domain.service.impl.SafeboxServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class BeanConfig {


    @Bean
    public SafeboxService safeboxService(UserService userService, SafeboxServiceService safeboxServiceService,
                                         TokenService tokenService) {
        return new SafeboxServiceImpl(userService, safeboxServiceService, tokenService);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
