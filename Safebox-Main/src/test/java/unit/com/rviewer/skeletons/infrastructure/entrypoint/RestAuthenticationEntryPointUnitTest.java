package unit.com.rviewer.skeletons.infrastructure.entrypoint;

import com.rviewer.skeletons.infrastructure.entrypoint.RestAuthenticationEntryPoint;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ExtendWith(MockitoExtension.class)
class RestAuthenticationEntryPointUnitTest {

    @InjectMocks
    private RestAuthenticationEntryPoint entryPoint;

    @Mock
    private HandlerExceptionResolver handlerExceptionResolver;

    @Test
    void commenceUnitTest() {
        HttpServletRequest request = new MockHttpServletRequest();
        HttpServletResponse response = new MockHttpServletResponse();
        AuthenticationException authException = new AuthenticationServiceException("");

        entryPoint.commence(request, response, authException);

        Mockito.verify(handlerExceptionResolver).resolveException(request, response, null, authException);
    }
}
