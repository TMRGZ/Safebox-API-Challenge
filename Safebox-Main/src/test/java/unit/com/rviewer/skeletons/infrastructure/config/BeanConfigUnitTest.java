package unit.com.rviewer.skeletons.infrastructure.config;

import com.rviewer.skeletons.domain.service.SafeboxHolderService;
import com.rviewer.skeletons.domain.service.SafeboxService;
import com.rviewer.skeletons.domain.service.TokenService;
import com.rviewer.skeletons.domain.service.UserService;
import com.rviewer.skeletons.infrastructure.config.BeanConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class BeanConfigUnitTest {

    @InjectMocks
    private BeanConfig beanConfig;

    @Test
    void safeboxServiceUnitTest() {
        UserService userService = Mockito.mock(UserService.class);
        SafeboxHolderService safeboxHolderService = Mockito.mock(SafeboxHolderService.class);
        TokenService tokenService = Mockito.mock(TokenService.class);

        SafeboxService safeboxService = beanConfig.safeboxService(userService, safeboxHolderService, tokenService);

        Assertions.assertNotNull(safeboxService);
    }

    @Test
    void restTemplateUnitTest() {
        Assertions.assertNotNull(beanConfig.restTemplate());
    }
}
