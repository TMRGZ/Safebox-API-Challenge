package unit.com.rviewer.skeletons.infrastructure.config;

import com.rviewer.skeletons.infrastructure.config.RabbitStreamConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.Message;

import java.util.function.Function;

@ExtendWith(MockitoExtension.class)
class RabbitStreamConfigUnitTest {

    @InjectMocks
    private RabbitStreamConfig rabbitStreamConfig;

    @Test
    void createdUserUnitTest() {
        Function<Message<String>, String> messageStringFunction = rabbitStreamConfig.createdUser();
        Assertions.assertNotNull(messageStringFunction);
    }
}
