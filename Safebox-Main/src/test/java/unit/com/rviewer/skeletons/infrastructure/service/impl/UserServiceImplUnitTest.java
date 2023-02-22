package unit.com.rviewer.skeletons.infrastructure.service.impl;


import com.rviewer.skeletons.domain.exception.ExternalServiceException;
import com.rviewer.skeletons.domain.exception.SafeboxAlreadyExistsException;
import com.rviewer.skeletons.domain.exception.SafeboxMainException;
import com.rviewer.skeletons.domain.model.User;
import com.rviewer.skeletons.infrastructure.mapper.UserMapper;
import com.rviewer.skeletons.infrastructure.rest.safebox.auth.UserApi;
import com.rviewer.skeletons.infrastructure.rest.safebox.auth.model.AuthRegisteredUserDto;
import com.rviewer.skeletons.infrastructure.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

@ExtendWith(MockitoExtension.class)
class UserServiceImplUnitTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserApi userApi;

    @Mock
    private UserMapper userMapper;

    @Test
    void createUserUnitTest() {
        String username = "TEST";
        String password = "TEST";
        AuthRegisteredUserDto userDto = new AuthRegisteredUserDto().id("ID");
        Mockito.when(userApi.postUser(Mockito.any())).thenReturn(userDto);
        Mockito.when(userMapper.map(userDto)).thenReturn(new User());

        userService.createUser(username, password);

        Mockito.verify(userApi).postUser(Mockito.any());
        Mockito.verify(userMapper).map(userDto);
    }

    @Test
    void createUser_safeboxAlreadyExistsException_UnitTest() {
        String username = "TEST";
        String password = "TEST";
        Mockito.when(userApi.postUser(Mockito.any())).thenThrow(new HttpClientErrorException(HttpStatus.CONFLICT));

        Assertions.assertThrows(SafeboxAlreadyExistsException.class, () -> userService.createUser(username, password));

        Mockito.verify(userApi).postUser(Mockito.any());
        Mockito.verify(userMapper, Mockito.never()).map(Mockito.any(AuthRegisteredUserDto.class));
    }

    @Test
    void createUser_unknownClientErrorException_UnitTest() {
        String username = "TEST";
        String password = "TEST";
        Mockito.when(userApi.postUser(Mockito.any())).thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        Assertions.assertThrows(SafeboxMainException.class, () -> userService.createUser(username, password));

        Mockito.verify(userApi).postUser(Mockito.any());
        Mockito.verify(userMapper, Mockito.never()).map(Mockito.any(AuthRegisteredUserDto.class));
    }

    @Test
    void createUser_externalServiceException_UnitTest() {
        String username = "TEST";
        String password = "TEST";
        Mockito.when(userApi.postUser(Mockito.any())).thenThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

        Assertions.assertThrows(ExternalServiceException.class, () -> userService.createUser(username, password));

        Mockito.verify(userApi).postUser(Mockito.any());
        Mockito.verify(userMapper, Mockito.never()).map(Mockito.any(AuthRegisteredUserDto.class));
    }
}
