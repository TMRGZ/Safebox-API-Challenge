package unit.com.rviewer.skeletons.infrastructure.config;

import com.rviewer.skeletons.infrastructure.config.SafeboxServiceMessagingConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SafeboxServiceMessagingConfigUnitTest {

    @Test
    void creationUnitTest() {
        SafeboxServiceMessagingConfig config = new SafeboxServiceMessagingConfig();

        config.setQueue("TEST");
        config.setExchange("TEST");
        config.setRoutingKey("TEST");

        Assertions.assertNotNull(config);
        Assertions.assertNotNull(config.getQueue());
        Assertions.assertNotNull(config.getExchange());
        Assertions.assertNotNull(config.getRoutingKey());
    }

}
