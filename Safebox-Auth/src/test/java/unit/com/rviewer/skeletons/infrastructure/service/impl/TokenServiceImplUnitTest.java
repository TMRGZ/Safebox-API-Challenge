package unit.com.rviewer.skeletons.infrastructure.service.impl;

import com.rviewer.skeletons.infrastructure.config.AppConfig;
import com.rviewer.skeletons.infrastructure.service.impl.TokenServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)
class TokenServiceImplUnitTest {

    @InjectMocks
    private TokenServiceImpl tokenService;

    @Mock
    private AppConfig appConfig;

    @AfterEach
    void cleanup() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void generateTokenUnitTest() {
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(securityContext.getAuthentication()).thenReturn(new UsernamePasswordAuthenticationToken("", ""));
        Mockito.when(appConfig.getTokenExpirationMinutes()).thenReturn(10);
        Mockito.when(appConfig.getTokenSecret()).thenReturn("SECRET_SECRET_SECRET_SECRET_SECRET");

        String generateToken = tokenService.generate();

        Mockito.verify(appConfig).getTokenExpirationMinutes();
        Mockito.verify(appConfig).getTokenSecret();

        Assertions.assertNotNull(generateToken);
    }

    @Test
    void decode_badToken_UnitTest() {
        String token = "BAD_TOKEN";
        Mockito.when(appConfig.getTokenSecret()).thenReturn("SECRET");

        String decoded = tokenService.decode(token);

        Mockito.verify(appConfig).getTokenSecret();
        Assertions.assertNull(decoded);
    }
}
