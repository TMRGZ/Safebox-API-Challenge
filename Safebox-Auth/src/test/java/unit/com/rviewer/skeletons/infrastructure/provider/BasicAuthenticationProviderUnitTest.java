package unit.com.rviewer.skeletons.infrastructure.provider;

import com.rviewer.skeletons.domain.exception.BadPasswordException;
import com.rviewer.skeletons.domain.exception.SafeboxAuthException;
import com.rviewer.skeletons.domain.exception.UserDoesNotExistException;
import com.rviewer.skeletons.domain.exception.UserIsLockedException;
import com.rviewer.skeletons.domain.service.LoginService;
import com.rviewer.skeletons.infrastructure.provider.BasicAuthenticationProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

@ExtendWith(MockitoExtension.class)
class BasicAuthenticationProviderUnitTest {

    @InjectMocks
    private BasicAuthenticationProvider basicAuthenticationProvider;

    @Mock
    private LoginService loginService;

    @Test
    void authenticateUnitTest() {
        String username = "TEST";
        String password = "TEST";
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getName()).thenReturn(username);
        Mockito.when(authentication.getCredentials()).thenReturn(password);

        Authentication authenticate = basicAuthenticationProvider.authenticate(authentication);

        Mockito.verify(loginService).loginUser(username, password);

        Assertions.assertNotNull(authenticate);
    }

    @Test
    void authenticate_userDoesNotExistException_UnitTest() {
        String username = "TEST";
        String password = "TEST";
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getName()).thenReturn(username);
        Mockito.when(authentication.getCredentials()).thenReturn(password);
        Mockito.doThrow(new UserDoesNotExistException()).when(loginService).loginUser(username, password);

        Authentication authenticate = basicAuthenticationProvider.authenticate(authentication);

        Mockito.verify(loginService).loginUser(username, password);

        Assertions.assertFalse(authenticate.isAuthenticated());
    }

    @Test
    void authenticate_badPasswordException_UnitTest() {
        String username = "TEST";
        String password = "TEST";
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getName()).thenReturn(username);
        Mockito.when(authentication.getCredentials()).thenReturn(password);
        Mockito.doThrow(new BadPasswordException()).when(loginService).loginUser(username, password);

        Authentication authenticate = basicAuthenticationProvider.authenticate(authentication);

        Mockito.verify(loginService).loginUser(username, password);

        Assertions.assertFalse(authenticate.isAuthenticated());
    }

    @Test
    void authenticate_lockedException_UnitTest() {
        String username = "TEST";
        String password = "TEST";
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getName()).thenReturn(username);
        Mockito.when(authentication.getCredentials()).thenReturn(password);
        Mockito.doThrow(new UserIsLockedException()).when(loginService).loginUser(username, password);

        Assertions.assertThrows(LockedException.class, () -> basicAuthenticationProvider.authenticate(authentication));

        Mockito.verify(loginService).loginUser(username, password);
    }

    @Test
    void authenticate_errorException_UnitTest() {
        String username = "TEST";
        String password = "TEST";
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getName()).thenReturn(username);
        Mockito.when(authentication.getCredentials()).thenReturn(password);
        Mockito.doThrow(new SafeboxAuthException()).when(loginService).loginUser(username, password);

        Assertions.assertThrows(InternalAuthenticationServiceException.class, () -> basicAuthenticationProvider.authenticate(authentication));

        Mockito.verify(loginService).loginUser(username, password);
    }

    @Test
    void supportsUnitTest() {
        Assertions.assertTrue(basicAuthenticationProvider.supports(UsernamePasswordAuthenticationToken.class));
    }
}
