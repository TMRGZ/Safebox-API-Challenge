package com.rviewer.skeletons.domain.service.impl;

import com.rviewer.skeletons.domain.exception.*;
import com.rviewer.skeletons.domain.model.event.EventResultEnum;
import com.rviewer.skeletons.domain.model.event.EventTypeEnum;
import com.rviewer.skeletons.domain.model.user.SafeboxUser;
import com.rviewer.skeletons.domain.model.user.SafeboxUserHistory;
import com.rviewer.skeletons.domain.repository.SafeboxUserRepository;
import com.rviewer.skeletons.domain.service.PasswordService;
import com.rviewer.skeletons.domain.sender.SafeboxHolderSender;
import com.rviewer.skeletons.domain.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.Date;

@Slf4j
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private PasswordService passwordService;

    private SafeboxUserRepository safeboxUserRepository;

    private SafeboxHolderSender safeboxHolderSender;


    @Override
    public SafeboxUser createUser(String username, String password) {
        log.info("Starting user cretion process for {}", username);

        safeboxUserRepository.findByUsername(username).ifPresent(user -> {
            log.error("Username {} is already registered", username);
            throw new UserAlreadyRegisteredException();
        });

        SafeboxUser safeboxUser = new SafeboxUser();
        SafeboxUserHistory safeboxUserHistory = new SafeboxUserHistory();

        safeboxUser.setUsername(username);
        safeboxUser.setPassword(passwordService.encodePassword(password));
        safeboxUser.setSafeboxUserHistory(Collections.singletonList(safeboxUserHistory));

        safeboxUserHistory.setEventDate(new Date());
        safeboxUserHistory.setEventTypeEnum(EventTypeEnum.CREATION);
        safeboxUserHistory.setEventResultEnum(EventResultEnum.SUCCESSFUL);
        safeboxUserHistory.setLocked(false);
        safeboxUserHistory.setCurrentTries(0L);

        SafeboxUser savedUser = safeboxUserRepository.save(safeboxUser);

        log.info("User {} successfully created, sending creation event...", username);

        safeboxHolderSender.send(savedUser.getId());

        return savedUser;
    }
}
