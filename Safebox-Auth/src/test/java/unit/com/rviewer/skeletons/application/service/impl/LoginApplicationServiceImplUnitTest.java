package unit.com.rviewer.skeletons.application.service.impl;

import com.rviewer.skeletons.application.model.LoginResponseDto;
import com.rviewer.skeletons.application.service.impl.LoginApplicationServiceImpl;
import com.rviewer.skeletons.domain.service.TokenService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class LoginApplicationServiceImplUnitTest {

    @InjectMocks
    private LoginApplicationServiceImpl loginApplicationService;

    @Mock
    private TokenService tokenService;

    @Test
    void loginUserUnitTest() {
        String token = "TOKEN";
        Mockito.when(tokenService.generate()).thenReturn(token);

        ResponseEntity<LoginResponseDto> response = loginApplicationService.loginUser();

        Mockito.verify(tokenService).generate();

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getStatusCode());
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertNotNull(response.getBody().getToken());
        Assertions.assertEquals(token, response.getBody().getToken());
    }
}
