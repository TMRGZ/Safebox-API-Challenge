package unit.com.rviewer.skeletons.application.service.impl;

import com.rviewer.skeletons.application.model.LoginResponseDto;
import com.rviewer.skeletons.application.model.RegisteredUserDto;
import com.rviewer.skeletons.application.model.UserDto;
import com.rviewer.skeletons.application.service.impl.UserApplicationServiceImpl;
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
        UserDto userDto = new UserDto().username("TEST").password("TEST");
        SafeboxUser safeboxUser = new SafeboxUser();
        safeboxUser.setId("ID");
        Mockito.when(userService.createUser(Mockito.anyString(), Mockito.anyString())).thenReturn(safeboxUser);

        ResponseEntity<RegisteredUserDto> response = userApplicationService.createUser(userDto);

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getBody());
        Assertions.assertNotNull(response.getBody().getId());
        Assertions.assertNotNull(response.getStatusCode());
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Mockito.verify(userService).createUser(userDto.getUsername(), userDto.getPassword());
    }

    @Test
    void loginUserUnitTest() {
        String token = "TOKEN";
        String userId = "TEST";
        Mockito.when(userService.generateUserToken(Mockito.anyString())).thenReturn(token);

        ResponseEntity<LoginResponseDto> response = userApplicationService.loginUser(userId);

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getBody());
        Assertions.assertNotNull(response.getBody().getToken());
        Assertions.assertNotNull(response.getStatusCode());
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Mockito.verify(userService).generateUserToken(userId);
    }
}
