package com.rviewer.skeletons.domain.service.impl;

import com.rviewer.skeletons.domain.exception.*;
import com.rviewer.skeletons.domain.model.event.EventResultEnum;
import com.rviewer.skeletons.domain.model.event.EventTypeEnum;
import com.rviewer.skeletons.domain.model.user.User;
import com.rviewer.skeletons.domain.model.user.UserHistory;
import com.rviewer.skeletons.domain.repository.UserRepository;
import com.rviewer.skeletons.domain.service.TokenService;
import com.rviewer.skeletons.domain.service.UserService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.BooleanUtils;

import java.util.Collections;
import java.util.Date;

@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private TokenService tokenService;

    private UserRepository userRepository;


    @Override
    public User createUser(String username, String password) {
        userRepository.findByUsername(username).ifPresent(user -> {
            throw new UserAlreadyRegisteredException();
        });

        User user = new User();
        UserHistory userHistory = new UserHistory();

        user.setUsername(username);
        user.setPassword(password);
        user.setUserHistory(Collections.singletonList(userHistory));

        userHistory.setEventDate(new Date());
        userHistory.setEventTypeEnum(EventTypeEnum.CREATION);
        userHistory.setEventResultEnum(EventResultEnum.SUCCESSFUL);
        userHistory.setLocked(false);
        userHistory.setCurrentTries(0L);

        return userRepository.save(user);
    }

    @Override
    public String loginUser(String username, String password) {
        User userToLogin = userRepository.findByUsername(username)
                .orElseThrow(UserDoesNotExistException::new);
        UserHistory lastHistory = userToLogin.getUserHistory().stream()
                .findFirst().orElseThrow(SafeboxAuthException::new);

        UserHistory newHistory = new UserHistory();
        userToLogin.getUserHistory().add(newHistory);

        newHistory.setEventDate(new Date());
        newHistory.setEventTypeEnum(EventTypeEnum.LOGIN);
        newHistory.setEventResultEnum(EventResultEnum.SUCCESSFUL);
        newHistory.setLocked(lastHistory.getLocked());
        newHistory.setCurrentTries(lastHistory.getCurrentTries());

        boolean isLocked = BooleanUtils.isTrue(lastHistory.getLocked());
        boolean badPassword = !userToLogin.getPassword().equals(password);
        if (isLocked || badPassword) {
            newHistory.setEventResultEnum(EventResultEnum.FAILED);
            newHistory.setCurrentTries(newHistory.getCurrentTries() + 1);
            newHistory.setLocked(newHistory.getCurrentTries() >= 3);
        } else {
            newHistory.setCurrentTries(0L);
        }

        userToLogin = userRepository.save(userToLogin);

        if (isLocked) throw new UserIsLockedException();
        if (badPassword) throw new BadPasswordException();
        return tokenService.generateToken(userToLogin);
    }
}
