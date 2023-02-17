package unit.com.rviewer.skeletons.infrastructure.sender.impl;

import com.rviewer.skeletons.infrastructure.config.SafeboxServiceMessagingConfig;
import com.rviewer.skeletons.infrastructure.sender.impl.SafeboxServiceSenderImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.AmqpTemplate;

@ExtendWith(MockitoExtension.class)
class SafeboxServiceSenderImplUnitTest {

    @InjectMocks
    private SafeboxServiceSenderImpl safeboxServiceSender;

    @Mock
    private AmqpTemplate amqpTemplate;

    @Mock
    private SafeboxServiceMessagingConfig safeboxServiceMessagingConfig;

    @Test
    void sendUnitTest() {
        String userId = "USER";

        safeboxServiceSender.send(userId);

        Mockito.verify(safeboxServiceMessagingConfig).getExchange();
        Mockito.verify(safeboxServiceMessagingConfig).getRoutingKey();
        Mockito.verify(amqpTemplate).convertAndSend(Mockito.any(), Mockito.any(), Mockito.eq(userId));
    }
}
