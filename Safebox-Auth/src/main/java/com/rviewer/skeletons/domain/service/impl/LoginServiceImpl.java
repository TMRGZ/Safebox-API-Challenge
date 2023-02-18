package com.rviewer.skeletons.domain.service.impl;

import com.rviewer.skeletons.domain.exception.BadPasswordException;
import com.rviewer.skeletons.domain.exception.SafeboxAuthException;
import com.rviewer.skeletons.domain.exception.UserDoesNotExistException;
import com.rviewer.skeletons.domain.exception.UserIsLockedException;
import com.rviewer.skeletons.domain.model.event.EventResultEnum;
import com.rviewer.skeletons.domain.model.event.EventTypeEnum;
import com.rviewer.skeletons.domain.model.user.SafeboxUser;
import com.rviewer.skeletons.domain.model.user.SafeboxUserHistory;
import com.rviewer.skeletons.domain.repository.SafeboxUserRepository;
import com.rviewer.skeletons.domain.service.LoginService;
import com.rviewer.skeletons.domain.service.PasswordService;
import com.rviewer.skeletons.domain.service.TokenService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;

import java.util.Comparator;
import java.util.Date;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
public class LoginServiceImpl implements LoginService {

    private PasswordService passwordService;

    private SafeboxUserRepository safeboxUserRepository;

    private final int maxTries;

    @Override
    public void loginUser(String username, String password) {
        log.info("Retrieving user with username {}", username);

        SafeboxUser safeboxUserToLogin = safeboxUserRepository.findByUsername(username)
                .orElseThrow(UserDoesNotExistException::new);
        SafeboxUserHistory lastHistory = safeboxUserToLogin.getSafeboxUserHistory().stream()
                .max(Comparator.comparing(SafeboxUserHistory::getEventDate))
                .orElseThrow(SafeboxAuthException::new);

        log.info("User {} successfully retrieved", username);

        SafeboxUserHistory newHistory = new SafeboxUserHistory();
        safeboxUserToLogin.getSafeboxUserHistory().add(newHistory);

        newHistory.setEventDate(new Date());
        newHistory.setEventTypeEnum(EventTypeEnum.LOGIN);
        newHistory.setEventResultEnum(EventResultEnum.SUCCESSFUL);
        newHistory.setLocked(lastHistory.getLocked());
        newHistory.setCurrentTries(lastHistory.getCurrentTries());

        boolean isLocked = BooleanUtils.isTrue(lastHistory.getLocked());
        boolean badPassword = !passwordService.checkPassword(safeboxUserToLogin.getPassword(), password);
        if (isLocked || badPassword) {
            newHistory.setEventResultEnum(EventResultEnum.FAILED);
            newHistory.setCurrentTries(newHistory.getCurrentTries() + 1);
            newHistory.setLocked(newHistory.getCurrentTries() >= maxTries);

            log.info("User can't login locked={} with tries={}", newHistory.getLocked(), newHistory.getCurrentTries());

        } else {
            newHistory.setCurrentTries(0L);
            log.info("User {} successfully logged in", username);
        }

        safeboxUserRepository.save(safeboxUserToLogin);

        if (isLocked) throw new UserIsLockedException();
        if (badPassword) throw new BadPasswordException();
    }
}
