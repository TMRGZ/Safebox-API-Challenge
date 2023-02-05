package unit.com.rviewer.skeletons.infrastructure.manager.impl;

import com.rviewer.skeletons.domain.exception.BadPasswordException;
import com.rviewer.skeletons.domain.exception.UserIsLockedException;
import com.rviewer.skeletons.domain.service.UserService;
import com.rviewer.skeletons.infrastructure.manager.impl.SafeboxAuthenticationManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;

@ExtendWith(MockitoExtension.class)
class SafeboxAuthenticationManagerUnitTest {

    @InjectMocks
    private SafeboxAuthenticationManager safeboxAuthenticationManager;

    @Mock
    private UserService userService;

    @Test
    void authenticateUnitTest() {
        String username = "TEST";
        String password = "TEST";
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getName()).thenReturn(username);
        Mockito.when(authentication.getCredentials()).thenReturn(password);

        Authentication authenticate = safeboxAuthenticationManager.authenticate(authentication);

        Mockito.verify(userService).loginUser(username, password);
        Assertions.assertNotNull(authenticate);
    }

    @Test
    void authenticate_lockedException_UnitTest() {
        String username = "TEST";
        String password = "TEST";
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getName()).thenReturn(username);
        Mockito.when(authentication.getCredentials()).thenReturn(password);
        Mockito.doThrow(new UserIsLockedException()).when(userService).loginUser(username, password);

        Assertions.assertThrows(LockedException.class, () -> safeboxAuthenticationManager.authenticate(authentication));

        Mockito.verify(userService).loginUser(username, password);
    }

    @Test
    void authenticate_badCredentialsException_UnitTest() {
        String username = "TEST";
        String password = "TEST";
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getName()).thenReturn(username);
        Mockito.when(authentication.getCredentials()).thenReturn(password);
        Mockito.doThrow(new BadPasswordException()).when(userService).loginUser(username, password);

        Assertions.assertThrows(BadCredentialsException.class, () -> safeboxAuthenticationManager.authenticate(authentication));

        Mockito.verify(userService).loginUser(username, password);
    }
}
