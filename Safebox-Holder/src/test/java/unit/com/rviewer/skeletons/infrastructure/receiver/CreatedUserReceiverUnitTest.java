package unit.com.rviewer.skeletons.infrastructure.receiver;

import com.rviewer.skeletons.application.service.SafeboxApplicationService;
import com.rviewer.skeletons.infrastructure.config.CreatedUserQueueConfig;
import com.rviewer.skeletons.infrastructure.receiver.CreatedUserReceiver;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreatedUserReceiverUnitTest {

    @InjectMocks
    private CreatedUserReceiver createdUserReceiver;

    @Mock
    private SafeboxApplicationService safeboxApplicationService;

    @Mock
    private CreatedUserQueueConfig createdUserQueueConfig;

    @Test
    void receiveUnitTest() {
        String owner = "TEST";

        createdUserReceiver.receive(owner);

        Mockito.verify(safeboxApplicationService).createSafebox(owner);
    }
}
