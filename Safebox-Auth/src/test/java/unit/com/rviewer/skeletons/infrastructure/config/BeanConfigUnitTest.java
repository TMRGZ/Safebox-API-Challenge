package unit.com.rviewer.skeletons.infrastructure.config;

import com.rviewer.skeletons.domain.repository.SafeboxUserRepository;
import com.rviewer.skeletons.domain.service.PasswordService;
import com.rviewer.skeletons.domain.service.TokenService;
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
        TokenService tokenService = Mockito.mock(TokenService.class);
        PasswordService passwordService = Mockito.mock(PasswordService.class);
        SafeboxUserRepository userRepository = Mockito.mock(SafeboxUserRepository.class);

        Assertions.assertNotNull(beanConfig.userService(tokenService, passwordService, userRepository));
    }
}
