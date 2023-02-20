package unit.com.rviewer.skeletons.infrastructure.provider.impl;

import com.rviewer.skeletons.domain.exception.BadTokenException;
import com.rviewer.skeletons.domain.exception.ExternalServiceException;
import com.rviewer.skeletons.domain.model.User;
import com.rviewer.skeletons.domain.service.TokenService;
import com.rviewer.skeletons.infrastructure.provider.impl.JwtProviderImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;

@ExtendWith(MockitoExtension.class)
class JwtProviderImplUnitTest {

    @InjectMocks
    private JwtProviderImpl jwtProvider;

    @Mock
    private TokenService tokenService;

    @Test
    void authenticateUnitTest() {
        String token = "TOKEN";
        User user = new User();
        user.setName("TEST");
        Authentication authentication = new BearerTokenAuthenticationToken(token);
        Mockito.when(tokenService.decodeToken(token)).thenReturn(user);

        Authentication authenticate = jwtProvider.authenticate(authentication);

        Mockito.verify(tokenService).decodeToken(token);

        Assertions.assertNotNull(authenticate);
        Assertions.assertNotNull(authenticate.getDetails());
        Assertions.assertEquals(user.getName(), authenticate.getDetails());
    }

    @Test
    void authenticate_badTokenException_UnitTest() {
        String token = "TOKEN";
        Authentication authentication = new BearerTokenAuthenticationToken(token);
        Mockito.when(tokenService.decodeToken(token)).thenThrow(new BadTokenException());

        Authentication authenticate = jwtProvider.authenticate(authentication);

        Assertions.assertFalse(authenticate.isAuthenticated());

        Mockito.verify(tokenService).decodeToken(token);
    }

    @Test
    void authenticate_externalServiceException_UnitTest() {
        String token = "TOKEN";
        Authentication authentication = new BearerTokenAuthenticationToken(token);
        Mockito.when(tokenService.decodeToken(token)).thenThrow(new ExternalServiceException());

        Assertions.assertThrows(InternalAuthenticationServiceException.class, () -> jwtProvider.authenticate(authentication));

        Mockito.verify(tokenService).decodeToken(token);
    }

    @Test
    void supportsUnitTest() {
        Assertions.assertTrue(jwtProvider.supports(BearerTokenAuthenticationToken.class));
    }
}
