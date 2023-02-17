package unit.com.rviewer.skeletons.infrastructure.controller.impl;

import com.rviewer.skeletons.application.service.LoginApplicationService;
import com.rviewer.skeletons.infrastructure.controller.impl.LoginApiImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LoginApiImplUnitTest {

    @InjectMocks
    private LoginApiImpl loginApi;

    @Mock
    private LoginApplicationService loginApplicationService;

    @Test
    void loginUserUnitTest() {
        loginApi.loginUser();

        Mockito.verify(loginApplicationService).loginUser();
    }
}
