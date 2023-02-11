package unit.com.rviewer.skeletons.infrastructure.provider.impl;

import com.rviewer.skeletons.domain.service.LoginService;
import com.rviewer.skeletons.infrastructure.provider.impl.BasicAuthProviderImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
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
        Assertions.assertNotNull(authenticate.getPrincipal());
        Assertions.assertEquals(username, authenticate.getPrincipal());
        Assertions.assertNotNull(authenticate.getCredentials());
        Assertions.assertEquals(password, authenticate.getCredentials());
        Assertions.assertNotNull(authenticate.getDetails());
        Assertions.assertEquals(token, authenticate.getDetails());
    }

    @Test
    void supportsUnitTest() {
        Assertions.assertTrue(basicAuthProvider.supports(UsernamePasswordAuthenticationToken.class));
    }
}
