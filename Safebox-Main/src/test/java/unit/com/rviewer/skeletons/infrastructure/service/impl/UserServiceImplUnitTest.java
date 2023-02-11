package unit.com.rviewer.skeletons.infrastructure.service.impl;

import com.rviewer.skeletons.application.model.safebox.auth.AuthLoginResponseDto;
import com.rviewer.skeletons.application.model.safebox.auth.AuthRegisteredUserDto;
import com.rviewer.skeletons.infrastructure.rest.safebox.auth.UserApi;
import com.rviewer.skeletons.infrastructure.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceImplUnitTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserApi userApi;

    @Test
    void createUserUnitTest() {
        String username = "TEST";
        String password = "TEST";
        Mockito.when(userApi.postUser(Mockito.any())).thenReturn(new AuthRegisteredUserDto().id("ID"));

        String userId = userService.createUser(username, password);

        Assertions.assertNotNull(userId);
    }
}
