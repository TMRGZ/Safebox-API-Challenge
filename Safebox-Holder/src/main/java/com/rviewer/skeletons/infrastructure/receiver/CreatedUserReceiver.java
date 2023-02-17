package com.rviewer.skeletons.infrastructure.receiver;

import com.rviewer.skeletons.application.service.SafeboxApplicationService;
import com.rviewer.skeletons.infrastructure.config.CreatedUserQueueConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CreatedUserReceiver {

    @Autowired
    private SafeboxApplicationService safeboxService;

    @Autowired
    private CreatedUserQueueConfig createdUserQueueConfig;

    @RabbitListener(queues = "#{createdUserQueueConfig.getQueue()}")
    public void receive(String owner) {
        safeboxService.createSafebox(owner);
    }
}
