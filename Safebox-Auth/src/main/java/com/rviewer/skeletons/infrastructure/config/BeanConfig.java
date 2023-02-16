package com.rviewer.skeletons.infrastructure.config;

import com.rviewer.skeletons.domain.repository.SafeboxUserRepository;
import com.rviewer.skeletons.domain.service.LoginService;
import com.rviewer.skeletons.domain.service.PasswordService;
import com.rviewer.skeletons.domain.sender.SafeboxServiceSender;
import com.rviewer.skeletons.domain.service.TokenService;
import com.rviewer.skeletons.domain.service.UserService;
import com.rviewer.skeletons.domain.service.impl.LoginServiceImpl;
import com.rviewer.skeletons.domain.service.impl.UserServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    public UserService userService(PasswordService passwordService, SafeboxUserRepository userRepository, SafeboxServiceSender safeboxServiceSender) {
        return new UserServiceImpl(passwordService, userRepository, safeboxServiceSender);
    }

    @Bean
    public LoginService loginService(PasswordService passwordService, SafeboxUserRepository safeboxUserRepository) {
        return new LoginServiceImpl(passwordService, safeboxUserRepository);
    }
}
