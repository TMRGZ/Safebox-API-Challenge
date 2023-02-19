package unit.com.rviewer.skeletons.infrastructure.provider;

import com.rviewer.skeletons.domain.service.TokenService;
import com.rviewer.skeletons.infrastructure.provider.JwtAuthenticationProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationProviderUnitTest {

    @InjectMocks
    private JwtAuthenticationProvider jwtAuthenticationProvider;

    @Mock
    private TokenService tokenService;

    @Test
    void authenticateUnitTest() {
        String token = "TOKEN";
        String username = "USER";
        Authentication authentication = new BearerTokenAuthenticationToken(token);
        Mockito.when(tokenService.decode(token)).thenReturn(username);

        Authentication authenticate = jwtAuthenticationProvider.authenticate(authentication);

        Mockito.verify(tokenService).decode(token);

        Assertions.assertNotNull(authenticate);
        Assertions.assertNotNull(authenticate.getPrincipal());
        Assertions.assertNotNull(authenticate.getDetails());
        Assertions.assertEquals(username, authenticate.getDetails());
    }

    @Test
    void supportsUnitTest() {
        Assertions.assertTrue(jwtAuthenticationProvider.supports(BearerTokenAuthenticationToken.class));
    }
}
