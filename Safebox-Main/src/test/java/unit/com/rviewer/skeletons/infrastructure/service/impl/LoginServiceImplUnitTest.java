package unit.com.rviewer.skeletons.infrastructure.service.impl;

import com.rviewer.skeletons.domain.exception.*;
import com.rviewer.skeletons.infrastructure.rest.safebox.auth.LoginApi;
import com.rviewer.skeletons.infrastructure.rest.safebox.auth.invoker.ApiClient;
import com.rviewer.skeletons.infrastructure.rest.safebox.auth.model.AuthLoginResponseDto;
import com.rviewer.skeletons.infrastructure.service.impl.LoginServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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
class LoginServiceImplUnitTest {

    @InjectMocks
    private LoginServiceImpl loginService;

    @Mock
    private LoginApi loginApi;

    @BeforeEach
    void setup() {
        Mockito.when(loginApi.getApiClient()).thenReturn(Mockito.mock(ApiClient.class));
    }

    @Test
    void loginUserUnitTest() {
        String username = "TEST";
        String password = "TEST";
        String token = "TOKEN";
        Mockito.when(loginApi.loginUser()).thenReturn(new AuthLoginResponseDto().token(token));

        String response = loginService.loginUser(username, password);

        Mockito.verify(loginApi).loginUser();

        Assertions.assertNotNull(response);
        Assertions.assertEquals(token, response);
    }

    @Test
    void loginUser_userDoesNotExistException_UnitTest() {
        String username = "TEST";
        String password = "TEST";
        Mockito.when(loginApi.loginUser()).thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        Assertions.assertThrows(UserDoesNotExistException.class, () -> loginService.loginUser(username, password));

        Mockito.verify(loginApi).loginUser();
    }

    @Test
    void loginUser_userUnauthorizedException_UnitTest() {
        String username = "TEST";
        String password = "TEST";
        Mockito.when(loginApi.loginUser()).thenThrow(new HttpClientErrorException(HttpStatus.UNAUTHORIZED));

        Assertions.assertThrows(UserIsUnauthorizedException.class, () -> loginService.loginUser(username, password));

        Mockito.verify(loginApi).loginUser();
    }

    @Test
    void loginUser_userLockedException_UnitTest() {
        String username = "TEST";
        String password = "TEST";
        Mockito.when(loginApi.loginUser()).thenThrow(new HttpClientErrorException(HttpStatus.LOCKED));

        Assertions.assertThrows(UserIsLockedException.class, () -> loginService.loginUser(username, password));

        Mockito.verify(loginApi).loginUser();
    }

    @Test
    void loginUser_unknownClientException_UnitTest() {
        String username = "TEST";
        String password = "TEST";
        Mockito.when(loginApi.loginUser()).thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        Assertions.assertThrows(SafeboxMainException.class, () -> loginService.loginUser(username, password));

        Mockito.verify(loginApi).loginUser();
    }

    @Test
    void loginUser_errorException_UnitTest() {
        String username = "TEST";
        String password = "TEST";
        Mockito.when(loginApi.loginUser()).thenThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

        Assertions.assertThrows(ExternalServiceException.class, () -> loginService.loginUser(username, password));

        Mockito.verify(loginApi).loginUser();
    }
}
