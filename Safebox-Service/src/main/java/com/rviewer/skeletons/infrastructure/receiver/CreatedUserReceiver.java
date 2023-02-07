package com.rviewer.skeletons.infrastructure.receiver;

import com.rviewer.skeletons.application.service.SafeboxApplicationService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CreatedUserReceiver {

    @Autowired
    private SafeboxApplicationService safeboxService;

    @RabbitListener(queues = "SAFEBOX_CREATED_USER.EXCHANGE.QUEUE")
    public void receive(String owner) {
        safeboxService.createSafebox(owner);
    }
}
