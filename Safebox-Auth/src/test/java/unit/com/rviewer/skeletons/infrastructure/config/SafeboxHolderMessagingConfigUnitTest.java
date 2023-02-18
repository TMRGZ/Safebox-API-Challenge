package unit.com.rviewer.skeletons.infrastructure.config;

import com.rviewer.skeletons.infrastructure.config.SafeboxHolderMessagingConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SafeboxHolderMessagingConfigUnitTest {

    @Test
    void creationUnitTest() {
        SafeboxHolderMessagingConfig config = new SafeboxHolderMessagingConfig();

        config.setQueue("TEST");
        config.setExchange("TEST");
        config.setRoutingKey("TEST");

        Assertions.assertNotNull(config);
        Assertions.assertNotNull(config.getQueue());
        Assertions.assertNotNull(config.getExchange());
        Assertions.assertNotNull(config.getRoutingKey());
    }

}
