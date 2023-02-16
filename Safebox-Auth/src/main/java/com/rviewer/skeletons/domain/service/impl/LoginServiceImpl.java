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
import org.apache.commons.lang3.BooleanUtils;

import java.util.Comparator;
import java.util.Date;
import java.util.Optional;

@AllArgsConstructor
public class LoginServiceImpl implements LoginService {

    private PasswordService passwordService;

    private SafeboxUserRepository safeboxUserRepository;

    @Override
    public void loginUser(String username, String password) {
        SafeboxUser safeboxUserToLogin = safeboxUserRepository.findByUsername(username)
                .orElseThrow(UserDoesNotExistException::new);
        SafeboxUserHistory lastHistory = safeboxUserToLogin.getSafeboxUserHistory().stream()
                .max(Comparator.comparing(SafeboxUserHistory::getEventDate))
                .orElseThrow(SafeboxAuthException::new);

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
            newHistory.setLocked(newHistory.getCurrentTries() >= 3);
        } else {
            newHistory.setCurrentTries(0L);
        }

        safeboxUserRepository.save(safeboxUserToLogin);

        if (isLocked) throw new UserIsLockedException();
        if (badPassword) throw new BadPasswordException();
    }
}
