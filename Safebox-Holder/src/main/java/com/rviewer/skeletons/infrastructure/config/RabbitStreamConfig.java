package com.rviewer.skeletons.infrastructure.config;

import com.rviewer.skeletons.infrastructure.receiver.CreatedUserReceiver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;

import java.util.function.Consumer;

@Configuration
public class RabbitStreamConfig {

    @Bean
    public Consumer<Message<String>> createdUser(CreatedUserReceiver receiver) {
        return createdUser -> receiver.receive(createdUser.getPayload());
    }
}
