package unit.com.rviewer.skeletons.domain.service.impl;

import com.rviewer.skeletons.domain.exception.UserAlreadyRegisteredException;
import com.rviewer.skeletons.domain.model.event.EventResultEnum;
import com.rviewer.skeletons.domain.model.event.EventTypeEnum;
import com.rviewer.skeletons.domain.model.user.SafeboxUser;
import com.rviewer.skeletons.domain.model.user.SafeboxUserHistory;
import com.rviewer.skeletons.domain.repository.SafeboxUserRepository;
import com.rviewer.skeletons.domain.sender.SafeboxHolderSender;
import com.rviewer.skeletons.domain.service.PasswordService;
import com.rviewer.skeletons.domain.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;


@ExtendWith(MockitoExtension.class)
class UserServiceImplUnitTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private PasswordService passwordService;

    @Mock
    private SafeboxUserRepository safeboxUserRepository;

    @Mock
    private SafeboxHolderSender safeboxHolderSender;

    @Test
    void createUserUnitTest() {
        String username = "TEST";
        String password = "TEST";
        Mockito.when(safeboxUserRepository.findByUsername(username)).thenReturn(Optional.empty());
        Mockito.when(passwordService.encodePassword(password)).thenReturn(password);
        Mockito.when(safeboxUserRepository.save(Mockito.any())).then(AdditionalAnswers.returnsFirstArg());

        SafeboxUser createdUser = userService.createUser(username, password);

        Mockito.verify(safeboxUserRepository).findByUsername(username);
        Mockito.verify(passwordService).encodePassword(password);
        Mockito.verify(safeboxUserRepository).save(Mockito.any());
        Mockito.verify(safeboxHolderSender).send(Mockito.any());

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
}
