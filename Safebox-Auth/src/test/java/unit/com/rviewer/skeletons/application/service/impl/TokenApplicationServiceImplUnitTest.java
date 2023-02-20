package unit.com.rviewer.skeletons.application.service.impl;

import com.rviewer.skeletons.application.model.UserDto;
import com.rviewer.skeletons.application.service.impl.TokenApplicationServiceImpl;
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
class TokenApplicationServiceImplUnitTest {

    @InjectMocks
    private TokenApplicationServiceImpl tokenApplicationService;

    @Mock
    private TokenService tokenService;

    @Test
    void decodeTokenUnitTest() {
        String username = "TEST";
        Mockito.when(tokenService.getDecodedUsername()).thenReturn(username);

        ResponseEntity<UserDto> response = tokenApplicationService.decodeToken();

        Mockito.verify(tokenService).getDecodedUsername();

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getStatusCode());
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertNotNull(response.getBody().getName());
        Assertions.assertEquals(username, response.getBody().getName());
    }
}
