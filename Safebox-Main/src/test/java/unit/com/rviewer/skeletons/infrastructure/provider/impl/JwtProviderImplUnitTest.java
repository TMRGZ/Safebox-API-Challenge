package unit.com.rviewer.skeletons.infrastructure.provider.impl;

import com.rviewer.skeletons.domain.exception.ExternalServiceException;
import com.rviewer.skeletons.domain.exception.UserDoesNotExistException;
import com.rviewer.skeletons.domain.exception.UserIsUnauthorizedException;
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
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.server.resource.BearerTokenAuthenticationToken;

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
        user.setUsername("TEST");
        Authentication authentication = new BearerTokenAuthenticationToken(token);
        Mockito.when(tokenService.decodeToken(token)).thenReturn(user);

        Authentication authenticate = jwtProvider.authenticate(authentication);

        Mockito.verify(tokenService).decodeToken(token);

        Assertions.assertNotNull(authenticate);
        Assertions.assertNotNull(authenticate.getDetails());
        Assertions.assertEquals(user.getUsername(), authenticate.getDetails());
    }

    @Test
    void authenticate_userDoesNotExistException_UnitTest() {
        String token = "TOKEN";
        Authentication authentication = new BearerTokenAuthenticationToken(token);
        Mockito.when(tokenService.decodeToken(token)).thenThrow(new UserDoesNotExistException());

        Assertions.assertThrows(UsernameNotFoundException.class, () -> jwtProvider.authenticate(authentication));

        Mockito.verify(tokenService).decodeToken(token);
    }

    @Test
    void authenticate_userUnauthorizedException_UnitTest() {
        String token = "TOKEN";
        Authentication authentication = new BearerTokenAuthenticationToken(token);
        Mockito.when(tokenService.decodeToken(token)).thenThrow(new UserIsUnauthorizedException());

        Assertions.assertThrows(BadCredentialsException.class, () -> jwtProvider.authenticate(authentication));

        Mockito.verify(tokenService).decodeToken(token);
    }

    @Test
    void authenticate_externalServiceException_UnitTest() {
        String token = "TOKEN";
        Authentication authentication = new BearerTokenAuthenticationToken(token);
        Mockito.when(tokenService.decodeToken(token)).thenThrow(new ExternalServiceException());

        Assertions.assertThrows(AuthenticationServiceException.class, () -> jwtProvider.authenticate(authentication));

        Mockito.verify(tokenService).decodeToken(token);
    }

    @Test
    void supportsUnitTest() {
        Assertions.assertTrue(jwtProvider.supports(BearerTokenAuthenticationToken.class));
    }
}
