package unit.com.rviewer.skeletons.infrastructure.sender.impl;

import com.rviewer.skeletons.infrastructure.config.SafeboxHolderMessagingConfig;
import com.rviewer.skeletons.infrastructure.sender.impl.SafeboxHolderSenderImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.MessageChannel;

@ExtendWith(MockitoExtension.class)
class SafeboxHolderSenderImplUnitTest {

    @InjectMocks
    private SafeboxHolderSenderImpl safeboxHolderSender;

    @Mock
    private MessageChannel messageChannel;

    @Mock
    private SafeboxHolderMessagingConfig safeboxHolderMessagingConfig;

    @Test
    void sendUnitTest() {
        String userId = "USER";

        safeboxHolderSender.send(userId);

        Mockito.verify(safeboxHolderMessagingConfig, Mockito.atLeastOnce()).getQueue();
        Mockito.verify(messageChannel).send(Mockito.any());
    }
}
