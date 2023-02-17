package unit.com.rviewer.skeletons.infrastructure.controller.impl;

import com.rviewer.skeletons.application.service.TokenApplicationService;
import com.rviewer.skeletons.infrastructure.controller.impl.TokenApiImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TokenApiImplUnitTest {

    @InjectMocks
    private TokenApiImpl tokenApi;

    @Mock
    private TokenApplicationService tokenApplicationService;

    @Test
    void decodeTokenUnitTest() {
        tokenApi.decodeToken();

        Mockito.verify(tokenApplicationService).decodeToken();
    }
}
