package unit.com.rviewer.skeletons.infrastructure.provider.impl;

import com.rviewer.skeletons.domain.exception.ExternalServiceException;
import com.rviewer.skeletons.domain.exception.UserDoesNotExistException;
import com.rviewer.skeletons.domain.exception.UserIsLockedException;
import com.rviewer.skeletons.domain.exception.UserIsUnauthorizedException;
import com.rviewer.skeletons.domain.service.LoginService;
import com.rviewer.skeletons.infrastructure.provider.impl.BasicAuthProviderImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

@ExtendWith(MockitoExtension.class)
class BasicAuthProviderImplUnitTest {

    @InjectMocks
    private BasicAuthProviderImpl basicAuthProvider;

    @Mock
    private LoginService loginService;

    @Test
    void authenticateUnitTest() {
        String username = "TEST";
        String password = "TEST";
        String token = "TOKEN";
        Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);
        Mockito.when(loginService.loginUser(username, password)).thenReturn(token);

        Authentication authenticate = basicAuthProvider.authenticate(authentication);

        Mockito.verify(loginService).loginUser(username, password);

        Assertions.assertNotNull(authenticate);
        Assertions.assertTrue(authenticate.isAuthenticated());
        Assertions.assertNotNull(authenticate.getPrincipal());
        Assertions.assertEquals(username, authenticate.getPrincipal());
        Assertions.assertNotNull(authenticate.getCredentials());
        Assertions.assertEquals(password, authenticate.getCredentials());
        Assertions.assertNotNull(authenticate.getDetails());
        Assertions.assertEquals(token, authenticate.getDetails());
    }

    @Test
    void authenticate_userDoesNotExistException_UnitTest() {
        String username = "TEST";
        String password = "TEST";
        Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);
        Mockito.when(loginService.loginUser(username, password)).thenThrow(new UserDoesNotExistException());

        Authentication authenticate = basicAuthProvider.authenticate(authentication);

        Mockito.verify(loginService).loginUser(username, password);

        Assertions.assertNotNull(authenticate);
        Assertions.assertFalse(authenticate.isAuthenticated());
    }

    @Test
    void authenticate_userDoesUnauthorizedException_UnitTest() {
        String username = "TEST";
        String password = "TEST";
        Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);
        Mockito.when(loginService.loginUser(username, password)).thenThrow(new UserIsUnauthorizedException());

        Authentication authenticate = basicAuthProvider.authenticate(authentication);

        Mockito.verify(loginService).loginUser(username, password);

        Assertions.assertNotNull(authenticate);
        Assertions.assertFalse(authenticate.isAuthenticated());
    }

    @Test
    void authenticate_userIsLockedException_UnitTest() {
        String username = "TEST";
        String password = "TEST";
        Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);
        Mockito.when(loginService.loginUser(username, password)).thenThrow(new UserIsLockedException());

        Assertions.assertThrows(LockedException.class, () -> basicAuthProvider.authenticate(authentication));

        Mockito.verify(loginService).loginUser(username, password);
    }

    @Test
    void authenticate_externalServiceException_UnitTest() {
        String username = "TEST";
        String password = "TEST";
        Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);
        Mockito.when(loginService.loginUser(username, password)).thenThrow(new ExternalServiceException());

        Assertions.assertThrows(AuthenticationServiceException.class, () -> basicAuthProvider.authenticate(authentication));

        Mockito.verify(loginService).loginUser(username, password);
    }

    @Test
    void supportsUnitTest() {
        Assertions.assertTrue(basicAuthProvider.supports(UsernamePasswordAuthenticationToken.class));
    }
}
