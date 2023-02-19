package com.rviewer.skeletons.infrastructure.config;

import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;

import java.util.function.Function;

@Configuration
public class RabbitStreamConfig {

    private static final String CREATED_USER_PRODUCER_BINDING = "createdUser-out-0";

    @Bean
    public Function<Message<String>, Message<String>> createdUser() {
        return createdUser -> createdUser;
    }

    @Bean
    public MessageChannel createdUserSender(StreamBridge streamBridge) {
        return (message, timeout) -> streamBridge.send(CREATED_USER_PRODUCER_BINDING, message);
    }
}
