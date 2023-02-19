package com.rviewer.skeletons.infrastructure.receiver;

import com.rviewer.skeletons.domain.exception.SafeboxAlreadyExistsException;
import com.rviewer.skeletons.domain.model.Safebox;
import com.rviewer.skeletons.domain.service.SafeboxService;
import com.rviewer.skeletons.infrastructure.config.CreatedUserQueueConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
public class CreatedUserReceiver {

    @Autowired
    private SafeboxService safeboxService;

    @Autowired
    private CreatedUserQueueConfig createdUserQueueConfig;

    @Transactional
    public void receive(String owner) {
        log.info("User {} created event received from {}, trying to create a safebox", owner, createdUserQueueConfig.getQueue());

        try {
            Safebox safebox = safeboxService.createSafebox(owner);
            log.info("Safebox {} successfully created", safebox.getId());

        } catch (SafeboxAlreadyExistsException e) {
            log.error("{} already has a safebox, creation process cancelled", owner);
        }
    }
}
