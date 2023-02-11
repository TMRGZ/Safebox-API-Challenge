package unit.com.rviewer.skeletons.infrastructure.config.endpoint;

import com.rviewer.skeletons.infrastructure.config.endpoint.ServiceEndpointConfig;
import com.rviewer.skeletons.infrastructure.rest.safebox.service.SafeboxServiceApi;
import com.rviewer.skeletons.infrastructure.rest.safebox.service.invoker.ApiClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ServiceEndpointConfigUnitTest {

    @InjectMocks
    private ServiceEndpointConfig serviceEndpointConfig;

    @Test
    void safeboxServiceApiUnitTest() {
        SafeboxServiceApi safeboxServiceApi = serviceEndpointConfig.safeboxServiceApi(new ApiClient());
        Assertions.assertNotNull(safeboxServiceApi);
    }
}
