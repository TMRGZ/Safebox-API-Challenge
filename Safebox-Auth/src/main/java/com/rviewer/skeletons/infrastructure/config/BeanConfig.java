package com.rviewer.skeletons.infrastructure.config;

import com.rviewer.skeletons.domain.repository.SafeboxUserRepository;
import com.rviewer.skeletons.domain.service.PasswordService;
import com.rviewer.skeletons.domain.sender.SafeboxServiceSender;
import com.rviewer.skeletons.domain.service.TokenService;
import com.rviewer.skeletons.domain.service.UserService;
import com.rviewer.skeletons.domain.service.impl.UserServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    public UserService userService(TokenService tokenService, PasswordService passwordService, SafeboxUserRepository userRepository, SafeboxServiceSender safeboxServiceSender) {
        return new UserServiceImpl(tokenService, passwordService, userRepository, safeboxServiceSender);
    }
}
