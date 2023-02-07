package com.rviewer.skeletons.infrastructure.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableRabbit
@Configuration
public class RabbitMqConfig {

    @Autowired
    private SafeboxServiceMessagingConfig safeboxServiceMessagingConfig;

    @Bean
    public Queue queue() {
        return new Queue(safeboxServiceMessagingConfig.getQueue(), false);
    }

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(safeboxServiceMessagingConfig.getExchange());
    }

    @Bean
    public Binding binding(Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(safeboxServiceMessagingConfig.getRoutingKey());
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
