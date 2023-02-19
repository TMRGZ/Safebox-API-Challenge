package unit.com.rviewer.skeletons.domain.service.impl;

import com.rviewer.skeletons.domain.exception.BadPasswordException;
import com.rviewer.skeletons.domain.exception.SafeboxAuthException;
import com.rviewer.skeletons.domain.exception.UserDoesNotExistException;
import com.rviewer.skeletons.domain.exception.UserIsLockedException;
import com.rviewer.skeletons.domain.model.event.EventResultEnum;
import com.rviewer.skeletons.domain.model.event.EventTypeEnum;
import com.rviewer.skeletons.domain.model.user.SafeboxUser;
import com.rviewer.skeletons.domain.model.user.SafeboxUserHistory;
import com.rviewer.skeletons.domain.repository.SafeboxUserRepository;
import com.rviewer.skeletons.domain.service.PasswordService;
import com.rviewer.skeletons.domain.service.impl.LoginServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class LoginServiceImplUnitTest {

    private LoginServiceImpl loginService;

    @Mock
    private PasswordService passwordService;

    @Mock
    private SafeboxUserRepository safeboxUserRepository;

    private static final int MAX_TRIES = 3;

    @BeforeEach
    void setup() {
        this.loginService = new LoginServiceImpl(passwordService, safeboxUserRepository, MAX_TRIES);
    }

    @Test
    void loginUserUnitTest() {
        String username = "TEST";
        String password = "TEST";
        SafeboxUser existingUser = new SafeboxUser();
        SafeboxUserHistory existingUserHistory = new SafeboxUserHistory();
        existingUser.setUsername(username);
        existingUser.setPassword(password);
        existingUser.setSafeboxUserHistory(new ArrayList<>());
        existingUser.getSafeboxUserHistory().add(existingUserHistory);
        existingUserHistory.setEventDate(Date.from(LocalDateTime.now().minusDays(1).atZone(ZoneOffset.systemDefault()).toInstant()));
        existingUserHistory.setEventTypeEnum(EventTypeEnum.CREATION);
        existingUserHistory.setEventResultEnum(EventResultEnum.SUCCESSFUL);
        existingUserHistory.setCurrentTries(0L);
        existingUserHistory.setLocked(false);
        Mockito.when(safeboxUserRepository.findByUsername(username)).thenReturn(Optional.of(existingUser));
        Mockito.when(passwordService.checkPassword(existingUser.getPassword(), password)).thenReturn(true);
        ArgumentCaptor<SafeboxUser> acSafeboxUser = ArgumentCaptor.forClass(SafeboxUser.class);
        int historySize = existingUser.getSafeboxUserHistory().size();

        loginService.loginUser(username, password);

        Mockito.verify(safeboxUserRepository).findByUsername(username);
        Mockito.verify(passwordService).checkPassword(existingUser.getPassword(), password);
        Mockito.verify(safeboxUserRepository).save(acSafeboxUser.capture());

        SafeboxUser capturedSafeboxUser = acSafeboxUser.getValue();
        Assertions.assertNotNull(capturedSafeboxUser);
        Assertions.assertNotNull(capturedSafeboxUser.getUsername());
        Assertions.assertEquals(username, capturedSafeboxUser.getUsername());
        Assertions.assertNotNull(capturedSafeboxUser.getPassword());
        Assertions.assertEquals(password, capturedSafeboxUser.getPassword());
        Assertions.assertNotNull(capturedSafeboxUser.getSafeboxUserHistory());
        Assertions.assertNotEquals(historySize, capturedSafeboxUser.getSafeboxUserHistory().size());
        Assertions.assertEquals(historySize + 1, capturedSafeboxUser.getSafeboxUserHistory().size());

        capturedSafeboxUser.getSafeboxUserHistory().stream()
                .reduce((h1, h2) -> {
                    Assertions.assertEquals(-1, h1.getEventDate().compareTo(h2.getEventDate()));
                    return h2;
                });

        SafeboxUserHistory lastHistory = capturedSafeboxUser.getSafeboxUserHistory().stream()
                .max(Comparator.comparing(SafeboxUserHistory::getEventDate))
                .orElse(null);

        Assertions.assertNotNull(lastHistory);
        Assertions.assertNotNull(lastHistory.getEventDate());
        Assertions.assertNotNull(lastHistory.getEventTypeEnum());
        Assertions.assertEquals(EventTypeEnum.LOGIN, lastHistory.getEventTypeEnum());
        Assertions.assertNotNull(lastHistory.getEventResultEnum());
        Assertions.assertEquals(EventResultEnum.SUCCESSFUL, lastHistory.getEventResultEnum());
        Assertions.assertNotNull(lastHistory.getLocked());
        Assertions.assertFalse(lastHistory.getLocked());
        Assertions.assertNotNull(lastHistory.getCurrentTries());
        Assertions.assertEquals(0, lastHistory.getCurrentTries());
    }

    @Test
    void loginUser_badPasswordException_UnitTest() {
        String username = "TEST";
        String password = "TEST";
        SafeboxUser existingUser = new SafeboxUser();
        SafeboxUserHistory existingUserHistory = new SafeboxUserHistory();
        existingUser.setUsername(username);
        existingUser.setPassword(password);
        existingUser.setSafeboxUserHistory(new ArrayList<>());
        existingUser.getSafeboxUserHistory().add(existingUserHistory);
        existingUserHistory.setEventDate(Date.from(LocalDateTime.now().minusDays(1).atZone(ZoneOffset.systemDefault()).toInstant()));
        existingUserHistory.setEventTypeEnum(EventTypeEnum.CREATION);
        existingUserHistory.setEventResultEnum(EventResultEnum.SUCCESSFUL);
        existingUserHistory.setCurrentTries(0L);
        existingUserHistory.setLocked(false);
        Mockito.when(safeboxUserRepository.findByUsername(username)).thenReturn(Optional.of(existingUser));
        Mockito.when(passwordService.checkPassword(existingUser.getPassword(), password)).thenReturn(false);
        ArgumentCaptor<SafeboxUser> acSafeboxUser = ArgumentCaptor.forClass(SafeboxUser.class);
        int historySize = existingUser.getSafeboxUserHistory().size();

        Assertions.assertThrows(BadPasswordException.class, () -> loginService.loginUser(username, password));

        Mockito.verify(safeboxUserRepository).findByUsername(username);
        Mockito.verify(passwordService).checkPassword(existingUser.getPassword(), password);
        Mockito.verify(safeboxUserRepository).save(acSafeboxUser.capture());

        SafeboxUser capturedSafeboxUser = acSafeboxUser.getValue();
        Assertions.assertNotNull(capturedSafeboxUser);
        Assertions.assertNotNull(capturedSafeboxUser.getUsername());
        Assertions.assertEquals(username, capturedSafeboxUser.getUsername());
        Assertions.assertNotNull(capturedSafeboxUser.getPassword());
        Assertions.assertEquals(password, capturedSafeboxUser.getPassword());
        Assertions.assertNotNull(capturedSafeboxUser.getSafeboxUserHistory());
        Assertions.assertNotEquals(historySize, capturedSafeboxUser.getSafeboxUserHistory().size());
        Assertions.assertEquals(historySize + 1, capturedSafeboxUser.getSafeboxUserHistory().size());

        capturedSafeboxUser.getSafeboxUserHistory().stream()
                .reduce((h1, h2) -> {
                    Assertions.assertEquals(-1, h1.getEventDate().compareTo(h2.getEventDate()));
                    return h2;
                });

        SafeboxUserHistory lastHistory = capturedSafeboxUser.getSafeboxUserHistory().stream()
                .max(Comparator.comparing(SafeboxUserHistory::getEventDate))
                .orElse(null);

        Assertions.assertNotNull(lastHistory);
        Assertions.assertNotNull(lastHistory.getEventDate());
        Assertions.assertNotNull(lastHistory.getEventTypeEnum());
        Assertions.assertEquals(EventTypeEnum.LOGIN, lastHistory.getEventTypeEnum());
        Assertions.assertNotNull(lastHistory.getEventResultEnum());
        Assertions.assertEquals(EventResultEnum.FAILED, lastHistory.getEventResultEnum());
        Assertions.assertNotNull(lastHistory.getLocked());
        Assertions.assertFalse(lastHistory.getLocked());
        Assertions.assertNotNull(lastHistory.getCurrentTries());
        Assertions.assertNotEquals(existingUserHistory.getCurrentTries(), lastHistory.getCurrentTries());
        Assertions.assertEquals(existingUserHistory.getCurrentTries() + 1, lastHistory.getCurrentTries());
    }

    @Test
    void loginUser_userLockedException_UnitTest() {
        String username = "TEST";
        String password = "TEST";
        SafeboxUser existingUser = new SafeboxUser();
        SafeboxUserHistory existingUserHistory = new SafeboxUserHistory();
        existingUser.setUsername(username);
        existingUser.setPassword(password);
        existingUser.setSafeboxUserHistory(new ArrayList<>());
        existingUser.getSafeboxUserHistory().add(existingUserHistory);
        existingUserHistory.setEventDate(Date.from(LocalDateTime.now().minusDays(1).atZone(ZoneOffset.systemDefault()).toInstant()));
        existingUserHistory.setEventTypeEnum(EventTypeEnum.CREATION);
        existingUserHistory.setEventResultEnum(EventResultEnum.SUCCESSFUL);
        existingUserHistory.setCurrentTries((long) MAX_TRIES);
        existingUserHistory.setLocked(true);
        Mockito.when(safeboxUserRepository.findByUsername(username)).thenReturn(Optional.of(existingUser));
        Mockito.when(passwordService.checkPassword(existingUser.getPassword(), password)).thenReturn(false);
        ArgumentCaptor<SafeboxUser> acSafeboxUser = ArgumentCaptor.forClass(SafeboxUser.class);
        int historySize = existingUser.getSafeboxUserHistory().size();

        Assertions.assertThrows(UserIsLockedException.class, () -> loginService.loginUser(username, password));

        Mockito.verify(safeboxUserRepository).findByUsername(username);
        Mockito.verify(passwordService).checkPassword(existingUser.getPassword(), password);
        Mockito.verify(safeboxUserRepository).save(acSafeboxUser.capture());

        SafeboxUser capturedSafeboxUser = acSafeboxUser.getValue();
        Assertions.assertNotNull(capturedSafeboxUser);
        Assertions.assertNotNull(capturedSafeboxUser.getUsername());
        Assertions.assertEquals(username, capturedSafeboxUser.getUsername());
        Assertions.assertNotNull(capturedSafeboxUser.getPassword());
        Assertions.assertEquals(password, capturedSafeboxUser.getPassword());
        Assertions.assertNotNull(capturedSafeboxUser.getSafeboxUserHistory());
        Assertions.assertNotEquals(historySize, capturedSafeboxUser.getSafeboxUserHistory().size());
        Assertions.assertEquals(historySize + 1, capturedSafeboxUser.getSafeboxUserHistory().size());

        capturedSafeboxUser.getSafeboxUserHistory().stream()
                .reduce((h1, h2) -> {
                    Assertions.assertEquals(-1, h1.getEventDate().compareTo(h2.getEventDate()));
                    return h2;
                });

        SafeboxUserHistory lastHistory = capturedSafeboxUser.getSafeboxUserHistory().stream()
                .max(Comparator.comparing(SafeboxUserHistory::getEventDate))
                .orElse(null);

        Assertions.assertNotNull(lastHistory);
        Assertions.assertNotNull(lastHistory.getEventDate());
        Assertions.assertNotNull(lastHistory.getEventTypeEnum());
        Assertions.assertEquals(EventTypeEnum.LOGIN, lastHistory.getEventTypeEnum());
        Assertions.assertNotNull(lastHistory.getEventResultEnum());
        Assertions.assertEquals(EventResultEnum.FAILED, lastHistory.getEventResultEnum());
        Assertions.assertNotNull(lastHistory.getLocked());
        Assertions.assertTrue(lastHistory.getLocked());
        Assertions.assertNotNull(lastHistory.getCurrentTries());
        Assertions.assertNotEquals(existingUserHistory.getCurrentTries(), lastHistory.getCurrentTries());
        Assertions.assertEquals(existingUserHistory.getCurrentTries() + 1, lastHistory.getCurrentTries());
    }

    @Test
    void loginUser_userDoesNotExistException_UnitTest() {
        String username = "TEST";
        String password = "TEST";
        Mockito.when(safeboxUserRepository.findByUsername(username)).thenReturn(Optional.empty());

        Assertions.assertThrows(UserDoesNotExistException.class, () -> loginService.loginUser(username, password));

        Mockito.verify(safeboxUserRepository).findByUsername(username);
        Mockito.verify(passwordService, Mockito.never()).checkPassword(Mockito.anyString(), Mockito.anyString());
        Mockito.verify(safeboxUserRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void loginUser_unexpectedException_UnitTest() {
        String username = "TEST";
        String password = "TEST";
        SafeboxUser existingUser = new SafeboxUser();
        existingUser.setSafeboxUserHistory(new ArrayList<>());
        Mockito.when(safeboxUserRepository.findByUsername(username)).thenReturn(Optional.of(existingUser));

        Assertions.assertThrows(SafeboxAuthException.class, () -> loginService.loginUser(username, password));

        Mockito.verify(safeboxUserRepository).findByUsername(username);
        Mockito.verify(passwordService, Mockito.never()).checkPassword(Mockito.anyString(), Mockito.anyString());
        Mockito.verify(safeboxUserRepository, Mockito.never()).save(Mockito.any());
    }

}
