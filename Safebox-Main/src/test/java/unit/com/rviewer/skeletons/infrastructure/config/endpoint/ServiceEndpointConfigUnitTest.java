package unit.com.rviewer.skeletons.infrastructure.config.endpoint;

import com.rviewer.skeletons.infrastructure.config.endpoint.ServiceEndpointConfig;
import com.rviewer.skeletons.infrastructure.rest.safebox.holder.SafeboxHolderApi;
import com.rviewer.skeletons.infrastructure.rest.safebox.holder.invoker.ApiClient;
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
    void safeboxHolderApiUnitTest() {
        SafeboxHolderApi safeboxHolderApi = serviceEndpointConfig.safeboxHolderApi(new ApiClient());
        Assertions.assertNotNull(safeboxHolderApi);
    }
}
