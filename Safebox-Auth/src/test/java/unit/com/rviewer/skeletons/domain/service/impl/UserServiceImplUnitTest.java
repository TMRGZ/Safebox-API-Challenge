package unit.com.rviewer.skeletons.domain.service.impl;

import com.rviewer.skeletons.domain.exception.*;
import com.rviewer.skeletons.domain.model.event.EventResultEnum;
import com.rviewer.skeletons.domain.model.event.EventTypeEnum;
import com.rviewer.skeletons.domain.model.user.SafeboxUser;
import com.rviewer.skeletons.domain.model.user.SafeboxUserHistory;
import com.rviewer.skeletons.domain.repository.SafeboxUserRepository;
import com.rviewer.skeletons.domain.sender.SafeboxServiceSender;
import com.rviewer.skeletons.domain.service.PasswordService;
import com.rviewer.skeletons.domain.service.TokenService;
import com.rviewer.skeletons.domain.service.impl.UserServiceImpl;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;


@ExtendWith(MockitoExtension.class)
class UserServiceImplUnitTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private TokenService tokenService;

    @Mock
    private PasswordService passwordService;

    @Mock
    private SafeboxUserRepository safeboxUserRepository;

    @Mock
    private SafeboxServiceSender safeboxServiceSender;

    @Test
    void createUserUnitTest() {
        String username = "TEST";
        String password = "TEST";
        Mockito.when(safeboxUserRepository.findByUsername(username)).thenReturn(Optional.empty());
        Mockito.when(passwordService.encodePassword(password)).thenReturn(password);
        Mockito.when(safeboxUserRepository.save(Mockito.any())).then(AdditionalAnswers.returnsFirstArg());

        SafeboxUser createdUser = userService.createUser(username, password);

        Assertions.assertNotNull(createdUser.getUsername());
        Assertions.assertNotNull(createdUser.getPassword());
        Assertions.assertFalse(createdUser.getSafeboxUserHistory().isEmpty());
        Assertions.assertEquals(1, createdUser.getSafeboxUserHistory().size());

        SafeboxUserHistory history = createdUser.getSafeboxUserHistory().get(0);
        Assertions.assertNotNull(history);
        Assertions.assertNotNull(history.getEventDate());
        Assertions.assertNotNull(history.getEventTypeEnum());
        Assertions.assertEquals(EventTypeEnum.CREATION, history.getEventTypeEnum());
        Assertions.assertNotNull(history.getEventResultEnum());
        Assertions.assertEquals(EventResultEnum.SUCCESSFUL, history.getEventResultEnum());
        Assertions.assertNotNull(history.getLocked());
        Assertions.assertFalse(history.getLocked());
        Assertions.assertNotNull(history.getCurrentTries());
        Assertions.assertEquals(0, history.getCurrentTries());

        Mockito.verify(safeboxUserRepository).findByUsername(username);
        Mockito.verify(passwordService).encodePassword(password);
        Mockito.verify(safeboxUserRepository).save(Mockito.any());
        Mockito.verify(safeboxServiceSender).send(Mockito.any());
    }

    @Test
    void createUser_userAlreadyExistsException_UnitTest() {
        String username = "TEST";
        String password = "TEST";
        Mockito.when(safeboxUserRepository.findByUsername(username)).thenReturn(Optional.of(new SafeboxUser()));

        Assertions.assertThrows(UserAlreadyRegisteredException.class, () -> userService.createUser(username, password));

        Mockito.verify(safeboxUserRepository).findByUsername(username);
        Mockito.verify(passwordService, Mockito.never()).encodePassword(password);
        Mockito.verify(safeboxUserRepository, Mockito.never()).save(Mockito.any());
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
        existingUserHistory.setEventDate(new Date());
        existingUserHistory.setEventTypeEnum(EventTypeEnum.CREATION);
        existingUserHistory.setEventResultEnum(EventResultEnum.SUCCESSFUL);
        existingUserHistory.setCurrentTries(0L);
        existingUserHistory.setLocked(false);
        Mockito.when(safeboxUserRepository.findByUsername(username)).thenReturn(Optional.of(existingUser));
        Mockito.when(passwordService.checkPassword(existingUser.getPassword(), password)).thenReturn(true);
        ArgumentCaptor<SafeboxUser> acSafeboxUser = ArgumentCaptor.forClass(SafeboxUser.class);
        int historySize = existingUser.getSafeboxUserHistory().size();

        Awaitility.await().atLeast(5, TimeUnit.SECONDS);
        userService.loginUser(username, password);

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
        existingUserHistory.setEventDate(new Date());
        existingUserHistory.setEventTypeEnum(EventTypeEnum.CREATION);
        existingUserHistory.setEventResultEnum(EventResultEnum.SUCCESSFUL);
        existingUserHistory.setCurrentTries(0L);
        existingUserHistory.setLocked(false);
        Mockito.when(safeboxUserRepository.findByUsername(username)).thenReturn(Optional.of(existingUser));
        Mockito.when(passwordService.checkPassword(existingUser.getPassword(), password)).thenReturn(false);
        ArgumentCaptor<SafeboxUser> acSafeboxUser = ArgumentCaptor.forClass(SafeboxUser.class);
        int historySize = existingUser.getSafeboxUserHistory().size();

        Awaitility.await().atLeast(5, TimeUnit.SECONDS);
        Assertions.assertThrows(BadPasswordException.class, () -> userService.loginUser(username, password));

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
        existingUserHistory.setEventDate(new Date());
        existingUserHistory.setEventTypeEnum(EventTypeEnum.CREATION);
        existingUserHistory.setEventResultEnum(EventResultEnum.SUCCESSFUL);
        existingUserHistory.setCurrentTries(3L);
        existingUserHistory.setLocked(true);
        Mockito.when(safeboxUserRepository.findByUsername(username)).thenReturn(Optional.of(existingUser));
        Mockito.when(passwordService.checkPassword(existingUser.getPassword(), password)).thenReturn(false);
        ArgumentCaptor<SafeboxUser> acSafeboxUser = ArgumentCaptor.forClass(SafeboxUser.class);
        int historySize = existingUser.getSafeboxUserHistory().size();

        Awaitility.await().atLeast(5, TimeUnit.SECONDS);
        Assertions.assertThrows(UserIsLockedException.class, () -> userService.loginUser(username, password));

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

        Assertions.assertThrows(UserDoesNotExistException.class, () -> userService.loginUser(username, password));

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

        Assertions.assertThrows(SafeboxAuthException.class, () -> userService.loginUser(username, password));

        Mockito.verify(safeboxUserRepository).findByUsername(username);
        Mockito.verify(passwordService, Mockito.never()).checkPassword(Mockito.anyString(), Mockito.anyString());
        Mockito.verify(safeboxUserRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void generateUserTokenUnitTest() {
        String userId = "TEST";
        Mockito.when(safeboxUserRepository.findById(userId)).thenReturn(Optional.of(new SafeboxUser()));

        userService.generateUserToken(userId);

        Mockito.verify(safeboxUserRepository).findById(userId);
        Mockito.verify(tokenService).generateToken(userId);
    }

    @Test
    void generateUserToken_userDoesNotExistException_UnitTest() {
        String userId = "TEST";
        Mockito.when(safeboxUserRepository.findById(userId)).thenReturn(Optional.empty());

        Assertions.assertThrows(UserDoesNotExistException.class, () -> userService.generateUserToken(userId));

        Mockito.verify(safeboxUserRepository).findById(userId);
        Mockito.verify(tokenService, Mockito.never()).generateToken(userId);
    }
}
