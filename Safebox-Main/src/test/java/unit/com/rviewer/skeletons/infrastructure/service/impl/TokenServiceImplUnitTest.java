package unit.com.rviewer.skeletons.infrastructure.service.impl;

import com.rviewer.skeletons.domain.exception.BadTokenException;
import com.rviewer.skeletons.domain.exception.ExternalServiceException;
import com.rviewer.skeletons.domain.exception.SafeboxMainException;
import com.rviewer.skeletons.domain.model.User;
import com.rviewer.skeletons.infrastructure.mapper.UserMapper;
import com.rviewer.skeletons.infrastructure.rest.safebox.auth.TokenApi;
import com.rviewer.skeletons.infrastructure.rest.safebox.auth.invoker.ApiClient;
import com.rviewer.skeletons.infrastructure.rest.safebox.auth.model.AuthUserDto;
import com.rviewer.skeletons.infrastructure.service.impl.TokenServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

@ExtendWith(MockitoExtension.class)
class TokenServiceImplUnitTest {

    @InjectMocks
    private TokenServiceImpl tokenService;

    @Mock
    private TokenApi tokenApi;

    @Mock
    private UserMapper userMapper;

    @Test
    void decodeTokenUnitTest() {
        String token = "TOKEN";
        Mockito.when(tokenApi.getApiClient()).thenReturn(Mockito.mock(ApiClient.class));
        AuthUserDto userDto = new AuthUserDto();
        Mockito.when(tokenApi.decodeToken()).thenReturn(userDto);
        Mockito.when(userMapper.map(userDto)).thenReturn(new User());

        tokenService.decodeToken(token);

        Mockito.verify(tokenApi).getApiClient();
        Mockito.verify(tokenApi).decodeToken();
        Mockito.verify(userMapper).map(userDto);
    }

    @Test
    void decodeToken_badTokenException_UnitTest() {
        String token = "TOKEN";
        Mockito.when(tokenApi.getApiClient()).thenReturn(Mockito.mock(ApiClient.class));
        Mockito.when(tokenApi.decodeToken()).thenThrow(new HttpClientErrorException(HttpStatus.FORBIDDEN));

        Assertions.assertThrows(BadTokenException.class, () -> tokenService.decodeToken(token));

        Mockito.verify(tokenApi).getApiClient();
        Mockito.verify(tokenApi).decodeToken();
        Mockito.verify(userMapper, Mockito.never()).map(Mockito.any(AuthUserDto.class));
    }

    @Test
    void decodeToken_unknownClientException_UnitTest() {
        String token = "TOKEN";
        Mockito.when(tokenApi.getApiClient()).thenReturn(Mockito.mock(ApiClient.class));
        Mockito.when(tokenApi.decodeToken()).thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        Assertions.assertThrows(SafeboxMainException.class, () -> tokenService.decodeToken(token));

        Mockito.verify(tokenApi).getApiClient();
        Mockito.verify(tokenApi).decodeToken();
        Mockito.verify(userMapper, Mockito.never()).map(Mockito.any(AuthUserDto.class));
    }

    @Test
    void decodeToken_externalServiceException_UnitTest() {
        String token = "TOKEN";
        Mockito.when(tokenApi.getApiClient()).thenReturn(Mockito.mock(ApiClient.class));
        Mockito.when(tokenApi.decodeToken()).thenThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

        Assertions.assertThrows(ExternalServiceException.class, () -> tokenService.decodeToken(token));

        Mockito.verify(tokenApi).getApiClient();
        Mockito.verify(tokenApi).decodeToken();
        Mockito.verify(userMapper, Mockito.never()).map(Mockito.any(AuthUserDto.class));
    }

    @Test
    void retrieveCurrentUserTokenUnitTest() {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken("TEST", "TEST");
        authenticationToken.setDetails("TOKEN");
        SecurityContext mockedSecurityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(mockedSecurityContext.getAuthentication()).thenReturn(authenticationToken);
        SecurityContextHolder.setContext(mockedSecurityContext);

        String token = tokenService.retrieveCurrenUserToken();

        Assertions.assertNotNull(token);
    }
}
