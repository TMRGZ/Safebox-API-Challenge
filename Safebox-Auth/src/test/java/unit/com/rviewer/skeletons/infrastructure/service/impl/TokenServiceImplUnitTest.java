package unit.com.rviewer.skeletons.infrastructure.service.impl;

import com.rviewer.skeletons.infrastructure.config.AppConfig;
import com.rviewer.skeletons.infrastructure.service.impl.TokenServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TokenServiceImplUnitTest {

    @InjectMocks
    private TokenServiceImpl tokenService;

    @Mock
    private AppConfig appConfig;

    @Test
    void generateTokenUnitTest() {
        String userId = "TEST";
        Mockito.when(appConfig.getTokenExpirationMinutes()).thenReturn(10);
        Mockito.when(appConfig.getTokenSecret()).thenReturn("SECRET_SECRET_SECRET_SECRET_SECRET");

        String generateToken = tokenService.generateToken(userId);

        Mockito.verify(appConfig).getTokenExpirationMinutes();
        Mockito.verify(appConfig).getTokenSecret();

        Assertions.assertNotNull(generateToken);
    }

    @Test
    void validateTokenUnitTest() {
        Mockito.when(appConfig.getTokenExpirationMinutes()).thenReturn(10);
        Mockito.when(appConfig.getTokenSecret()).thenReturn("SECRET_SECRET_SECRET_SECRET_SECRET");
        String token = tokenService.generateToken("TEST");

        boolean valid = tokenService.validateToken(token);

        Mockito.verify(appConfig, Mockito.atLeastOnce()).getTokenSecret();
        Assertions.assertTrue(valid);
    }

    @Test
    void validateToken_badToken_UnitTest() {
        String token = "BAD_TOKEN";
        Mockito.when(appConfig.getTokenSecret()).thenReturn("SECRET");

        boolean valid = tokenService.validateToken(token);

        Mockito.verify(appConfig).getTokenSecret();
        Assertions.assertFalse(valid);
    }
}
