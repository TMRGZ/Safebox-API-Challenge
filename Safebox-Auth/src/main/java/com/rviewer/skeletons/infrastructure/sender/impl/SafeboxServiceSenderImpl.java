package com.rviewer.skeletons.infrastructure.sender.impl;

import com.rviewer.skeletons.domain.sender.SafeboxServiceSender;
import com.rviewer.skeletons.infrastructure.config.SafeboxServiceMessagingConfig;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SafeboxServiceSenderImpl implements SafeboxServiceSender {

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private SafeboxServiceMessagingConfig safeboxServiceMessagingConfig;

    @Override
    public void send(String userId) {
        amqpTemplate.convertAndSend(safeboxServiceMessagingConfig.getExchange(), safeboxServiceMessagingConfig.getRoutingKey(), userId);
    }
}
