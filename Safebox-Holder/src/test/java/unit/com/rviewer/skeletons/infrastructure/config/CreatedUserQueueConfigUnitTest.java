package unit.com.rviewer.skeletons.infrastructure.config;

import com.rviewer.skeletons.infrastructure.config.CreatedUserQueueConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CreatedUserQueueConfigUnitTest {

    @Test
    void creationUnitTest() {
        CreatedUserQueueConfig createdUserQueueConfig = new CreatedUserQueueConfig();
        createdUserQueueConfig.setQueue("TEST");
        createdUserQueueConfig.setExchange("TEST");
        createdUserQueueConfig.setRoutingKey("TEST");

        Assertions.assertNotNull(createdUserQueueConfig);
        Assertions.assertNotNull(createdUserQueueConfig.getQueue());
        Assertions.assertNotNull(createdUserQueueConfig.getExchange());
        Assertions.assertNotNull(createdUserQueueConfig.getRoutingKey());
    }
}
