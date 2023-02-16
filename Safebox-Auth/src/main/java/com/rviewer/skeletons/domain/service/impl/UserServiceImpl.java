package com.rviewer.skeletons.domain.service.impl;

import com.rviewer.skeletons.domain.exception.*;
import com.rviewer.skeletons.domain.model.event.EventResultEnum;
import com.rviewer.skeletons.domain.model.event.EventTypeEnum;
import com.rviewer.skeletons.domain.model.user.SafeboxUser;
import com.rviewer.skeletons.domain.model.user.SafeboxUserHistory;
import com.rviewer.skeletons.domain.repository.SafeboxUserRepository;
import com.rviewer.skeletons.domain.service.PasswordService;
import com.rviewer.skeletons.domain.sender.SafeboxServiceSender;
import com.rviewer.skeletons.domain.service.TokenService;
import com.rviewer.skeletons.domain.service.UserService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.BooleanUtils;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Optional;

@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private PasswordService passwordService;

    private SafeboxUserRepository safeboxUserRepository;

    private SafeboxServiceSender safeboxServiceSender;


    @Override
    public SafeboxUser createUser(String username, String password) {
        safeboxUserRepository.findByUsername(username).ifPresent(user -> {
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
        safeboxServiceSender.send(savedUser.getId());

        return savedUser;
    }
}
