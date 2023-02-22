package com.rviewer.skeletons.infrastructure.sender.impl;

import com.rviewer.skeletons.domain.sender.SafeboxHolderSender;
import com.rviewer.skeletons.infrastructure.config.SafeboxHolderMessagingConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SafeboxHolderSenderImpl implements SafeboxHolderSender {

    @Autowired
    private StreamBridge streamBridge;

    @Autowired
    private SafeboxHolderMessagingConfig safeboxHolderMessagingConfig;

    @Override
    public void send(String userId) {
        log.info("Trying to send {} to {} event queue", userId, safeboxHolderMessagingConfig.getQueue());
        streamBridge.send(safeboxHolderMessagingConfig.getExchange(), MessageBuilder.withPayload(userId).build());
        log.info("Successfully sent {} to {}", userId, safeboxHolderMessagingConfig.getQueue());
    }
}
