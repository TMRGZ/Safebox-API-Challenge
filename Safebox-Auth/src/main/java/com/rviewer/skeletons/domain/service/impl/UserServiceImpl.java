package com.rviewer.skeletons.domain.service.impl;

import com.rviewer.skeletons.domain.exception.*;
import com.rviewer.skeletons.domain.model.event.EventResultEnum;
import com.rviewer.skeletons.domain.model.event.EventTypeEnum;
import com.rviewer.skeletons.domain.model.user.SafeboxUser;
import com.rviewer.skeletons.domain.model.user.SafeboxUserHistory;
import com.rviewer.skeletons.domain.repository.SafeboxUserRepository;
import com.rviewer.skeletons.domain.service.PasswordService;
import com.rviewer.skeletons.domain.service.TokenService;
import com.rviewer.skeletons.domain.service.UserService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.BooleanUtils;

import java.util.Collections;
import java.util.Date;

@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private TokenService tokenService;

    private PasswordService passwordService;

    private SafeboxUserRepository safeboxUserRepository;


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

        return safeboxUserRepository.save(safeboxUser);
    }

    @Override
    public void loginUser(String username, String password) {
        SafeboxUser safeboxUserToLogin = safeboxUserRepository.findByUsername(username)
                .orElseThrow(UserDoesNotExistException::new);
        SafeboxUserHistory lastHistory = safeboxUserToLogin.getSafeboxUserHistory().stream()
                .findFirst().orElseThrow(SafeboxAuthException::new);

        SafeboxUserHistory newHistory = new SafeboxUserHistory();
        safeboxUserToLogin.getSafeboxUserHistory().add(newHistory);

        newHistory.setEventDate(new Date());
        newHistory.setEventTypeEnum(EventTypeEnum.LOGIN);
        newHistory.setEventResultEnum(EventResultEnum.SUCCESSFUL);
        newHistory.setLocked(lastHistory.getLocked());
        newHistory.setCurrentTries(lastHistory.getCurrentTries());

        boolean isLocked = BooleanUtils.isTrue(lastHistory.getLocked());
        boolean badPassword = passwordService.checkPassword(safeboxUserToLogin.getPassword(), password);
        if (isLocked || badPassword) {
            newHistory.setEventResultEnum(EventResultEnum.FAILED);
            newHistory.setCurrentTries(newHistory.getCurrentTries() + 1);
            newHistory.setLocked(newHistory.getCurrentTries() >= 3);
        } else {
            newHistory.setCurrentTries(0L);
        }

        safeboxUserRepository.save(safeboxUserToLogin);

        if (isLocked) throw new UserIsLockedException();
        if (badPassword) throw new BadPasswordException();
    }

    @Override
    public String generateUserToken(String userId) {
        safeboxUserRepository.findById(userId).orElseThrow(UserDoesNotExistException::new);
        return tokenService.generateToken(userId);
    }
}
