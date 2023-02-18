package unit.com.rviewer.skeletons.infrastructure.sender.impl;

import com.rviewer.skeletons.infrastructure.config.SafeboxHolderMessagingConfig;
import com.rviewer.skeletons.infrastructure.sender.impl.SafeboxHolderSenderImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.AmqpTemplate;

@ExtendWith(MockitoExtension.class)
class SafeboxHolderSenderImplUnitTest {

    @InjectMocks
    private SafeboxHolderSenderImpl safeboxServiceSender;

    @Mock
    private AmqpTemplate amqpTemplate;

    @Mock
    private SafeboxHolderMessagingConfig safeboxHolderMessagingConfig;

    @Test
    void sendUnitTest() {
        String userId = "USER";

        safeboxServiceSender.send(userId);

        Mockito.verify(safeboxHolderMessagingConfig).getExchange();
        Mockito.verify(safeboxHolderMessagingConfig).getRoutingKey();
        Mockito.verify(amqpTemplate).convertAndSend(Mockito.any(), Mockito.any(), Mockito.eq(userId));
    }
}
