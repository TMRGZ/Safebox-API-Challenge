package unit.com.rviewer.skeletons.infrastructure.controller.impl;

import com.rviewer.skeletons.application.model.UserDto;
import com.rviewer.skeletons.application.service.UserApplicationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SafeboxAuthApiImplUnitTest {

    @InjectMocks
    private SafeboxAuthApiImpl safeboxAuthApi;

    @Mock
    private UserApplicationService userApplicationService;

    @Test
    void safeboxAuthIdLoginPostUnitTest() {
        String id = "TEST";

        safeboxAuthApi.safeboxAuthIdLoginPost(id);

        Mockito.verify(userApplicationService).loginUser(id);
    }

    @Test
    void safeboxAuthUserPostUnitTest() {
        UserDto userDto = new UserDto();

        safeboxAuthApi.safeboxAuthUserPost(userDto);

        Mockito.verify(userApplicationService).createUser(userDto);
    }
}
