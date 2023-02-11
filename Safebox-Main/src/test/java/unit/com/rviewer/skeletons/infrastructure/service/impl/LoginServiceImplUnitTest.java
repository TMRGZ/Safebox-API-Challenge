package unit.com.rviewer.skeletons.infrastructure.service.impl;

import com.rviewer.skeletons.application.model.safebox.auth.LoginResponseDto;
import com.rviewer.skeletons.infrastructure.rest.safebox.auth.LoginApi;
import com.rviewer.skeletons.infrastructure.rest.safebox.auth.invoker.ApiClient;
import com.rviewer.skeletons.infrastructure.service.impl.LoginServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LoginServiceImplUnitTest {

    @InjectMocks
    private LoginServiceImpl loginService;

    @Mock
    private LoginApi loginApi;

    @BeforeEach
    void setup() {
        loginApi.setApiClient(Mockito.mock(ApiClient.class));
    }

    @Test
    void loginUserUnitTest() {
        String username = "TEST";
        String password = "TEST";
        String token = "TOKEN";
        Mockito.when(loginApi.loginUser("")).thenReturn(new LoginResponseDto().token(token));

        loginService.loginUser(username, password);

        Mockito.verify(loginApi).loginUser("");
    }
}
