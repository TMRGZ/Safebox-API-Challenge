package com.rviewer.skeletons.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;

import java.util.function.Function;

@Configuration
public class RabbitStreamConfig {

    @Bean
    public Function<Message<String>, Message<String>> createdUser() {
        return createdUser -> createdUser;
    }
}
