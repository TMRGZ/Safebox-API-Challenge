package unit.com.rviewer.skeletons.infrastructure.config;

import com.rviewer.skeletons.domain.repository.SafeboxUserRepository;
import com.rviewer.skeletons.domain.sender.SafeboxHolderSender;
import com.rviewer.skeletons.domain.service.PasswordService;
import com.rviewer.skeletons.infrastructure.config.AppConfig;
import com.rviewer.skeletons.infrastructure.config.BeanConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BeanConfigUnitTest {

    @InjectMocks
    private BeanConfig beanConfig;

    @Test
    void userServiceUnitTest() {
        PasswordService passwordService = Mockito.mock(PasswordService.class);
        SafeboxUserRepository userRepository = Mockito.mock(SafeboxUserRepository.class);
        SafeboxHolderSender safeboxHolderSender = Mockito.mock(SafeboxHolderSender.class);

        Assertions.assertNotNull(beanConfig.userService(passwordService, userRepository, safeboxHolderSender));
    }

    @Test
    void loginServiceUnitTest() {
        PasswordService passwordService = Mockito.mock(PasswordService.class);
        SafeboxUserRepository userRepository = Mockito.mock(SafeboxUserRepository.class);
        AppConfig appConfig = Mockito.mock(AppConfig.class);
        Mockito.when(appConfig.getMaxTries()).thenReturn(0);

        Assertions.assertNotNull(beanConfig.loginService(passwordService, userRepository, appConfig));
    }
}
