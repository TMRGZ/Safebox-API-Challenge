package unit.com.rviewer.skeletons.application.service.impl;

import com.rviewer.skeletons.application.model.CreateUserDto;
import com.rviewer.skeletons.application.model.RegisteredUserDto;
import com.rviewer.skeletons.application.service.impl.UserApplicationServiceImpl;
import com.rviewer.skeletons.domain.exception.UserAlreadyRegisteredException;
import com.rviewer.skeletons.domain.model.user.SafeboxUser;
import com.rviewer.skeletons.domain.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class UserApplicationServiceImplUnitTest {

    @InjectMocks
    private UserApplicationServiceImpl userApplicationService;

    @Mock
    private UserService userService;

    @Test
    void createUserUnitTest() {
        CreateUserDto userDto = new CreateUserDto().username("TEST").password("TEST");
        SafeboxUser safeboxUser = new SafeboxUser();
        safeboxUser.setId("ID");
        Mockito.when(userService.createUser(Mockito.anyString(), Mockito.anyString())).thenReturn(safeboxUser);

        ResponseEntity<RegisteredUserDto> response = userApplicationService.createUser(userDto);

        Mockito.verify(userService).createUser(userDto.getUsername(), userDto.getPassword());

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getBody());
        Assertions.assertNotNull(response.getBody().getId());
        Assertions.assertNotNull(response.getStatusCode());
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void createUser_userAlreadyExisting_UnitTest() {
        CreateUserDto userDto = new CreateUserDto().username("TEST").password("TEST");
        Mockito.when(userService.createUser(userDto.getUsername(), userDto.getPassword())).thenThrow(new UserAlreadyRegisteredException());

        ResponseEntity<RegisteredUserDto> response = userApplicationService.createUser(userDto);

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getStatusCode());
        Assertions.assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        Assertions.assertNull(response.getBody());
    }
}
