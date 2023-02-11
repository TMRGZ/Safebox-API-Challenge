package unit.com.rviewer.skeletons.infrastructure.config.endpoint;

import com.rviewer.skeletons.infrastructure.config.endpoint.AuthEndpointConfig;
import com.rviewer.skeletons.infrastructure.rest.safebox.auth.LoginApi;
import com.rviewer.skeletons.infrastructure.rest.safebox.auth.UserApi;
import com.rviewer.skeletons.infrastructure.rest.safebox.auth.invoker.ApiClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AuthEndpointConfigUnitTest {

    @InjectMocks
    private AuthEndpointConfig authEndpointConfig;

    @Test
    void loginApiUnitTest() {
        LoginApi loginApi = authEndpointConfig.loginApi(new ApiClient());
        Assertions.assertNotNull(loginApi);
    }

    @Test
    void userApiUnitTest() {
        UserApi userApi = authEndpointConfig.userApi(new ApiClient());
        Assertions.assertNotNull(userApi);
    }
}
