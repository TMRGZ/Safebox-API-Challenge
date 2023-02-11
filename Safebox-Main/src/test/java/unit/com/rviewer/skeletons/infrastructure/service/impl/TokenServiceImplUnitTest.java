package unit.com.rviewer.skeletons.infrastructure.service.impl;

import com.rviewer.skeletons.infrastructure.service.impl.TokenServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)
class TokenServiceImplUnitTest {

    @InjectMocks
    private TokenServiceImpl tokenService;

    @BeforeEach
    void setup() {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken("TEST", "TEST");
        authenticationToken.setDetails("TOKEN");

        SecurityContext mockedSecurityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(mockedSecurityContext.getAuthentication()).thenReturn(authenticationToken);

        SecurityContextHolder.setContext(mockedSecurityContext);
    }

    @Test
    void retrieveCurrentUserTokenUnitTest() {
        String token = tokenService.retrieveCurrenUserToken();

        Assertions.assertNotNull(token);
    }
}
