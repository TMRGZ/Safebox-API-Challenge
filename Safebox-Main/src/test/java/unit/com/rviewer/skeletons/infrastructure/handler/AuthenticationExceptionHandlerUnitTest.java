package unit.com.rviewer.skeletons.infrastructure.handler;

import com.rviewer.skeletons.infrastructure.handler.AuthenticationExceptionHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class AuthenticationExceptionHandlerUnitTest {

    @InjectMocks
    private AuthenticationExceptionHandler handler;

    @Test
    void handleLockedExceptionUnitTest() {
        ResponseEntity<Void> response = handler.handleLockedException();

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getStatusCode());
        Assertions.assertEquals(HttpStatus.LOCKED, response.getStatusCode());
    }

    @Test
    void handleInternalAuthenticationServiceExceptionUnitTest() {
        ResponseEntity<Void> response = handler.handleInternalAuthenticationServiceException();

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getStatusCode());
        Assertions.assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void handleAuthenticationExceptionUnitTest() {
        ResponseEntity<Void> response = handler.handleAuthenticationException();

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getStatusCode());
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }
}
