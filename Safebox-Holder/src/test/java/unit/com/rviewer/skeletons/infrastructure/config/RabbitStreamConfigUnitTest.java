package unit.com.rviewer.skeletons.infrastructure.config;

import com.rviewer.skeletons.infrastructure.config.RabbitStreamConfig;
import com.rviewer.skeletons.infrastructure.receiver.CreatedUserReceiver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.Message;

import java.util.function.Consumer;

@ExtendWith(MockitoExtension.class)
class RabbitStreamConfigUnitTest {

    @InjectMocks
    private RabbitStreamConfig rabbitStreamConfig;

    @Mock
    private CreatedUserReceiver receiver;

    @Test
    void createdUserUnitTest() {
        Consumer<Message<String>> consumer = rabbitStreamConfig.createdUser(receiver);
        Assertions.assertNotNull(consumer);
    }
}
