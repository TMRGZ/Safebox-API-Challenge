package unit.com.rviewer.skeletons.infrastructure.controller.impl;

import com.rviewer.skeletons.application.model.CreateUserDto;
import com.rviewer.skeletons.application.service.UserApplicationService;
import com.rviewer.skeletons.infrastructure.controller.impl.UserApiImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserApiImplUnitTest {

    @InjectMocks
    private UserApiImpl userApi;

    @Mock
    private UserApplicationService userApplicationService;

    @Test
    void postUserUnitTest() {
        CreateUserDto userDto = new CreateUserDto();

        userApi.postUser(userDto);

        Mockito.verify(userApplicationService).createUser(userDto);
    }
}
