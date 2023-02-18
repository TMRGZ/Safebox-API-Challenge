package unit.com.rviewer.skeletons.infrastructure.receiver;

import com.rviewer.skeletons.domain.exception.SafeboxAlreadyExistsException;
import com.rviewer.skeletons.domain.model.Safebox;
import com.rviewer.skeletons.domain.service.SafeboxService;
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
    private SafeboxService safeboxService;

    @Mock
    private CreatedUserQueueConfig createdUserQueueConfig;

    @Test
    void receiveUnitTest() {
        String owner = "TEST";
        Safebox safebox = new Safebox();
        safebox.setId("ID");
        safebox.setOwner(owner);
        Mockito.when(safeboxService.createSafebox(owner)).thenReturn(safebox);

        createdUserReceiver.receive(owner);

        Mockito.verify(createdUserQueueConfig).getQueue();
        Mockito.verify(safeboxService).createSafebox(owner);
    }

    @Test
    void receive_safeboxAlreadyExistsException_UnitTest() {
        String owner = "TEST";
        Mockito.when(safeboxService.createSafebox(owner)).thenThrow(new SafeboxAlreadyExistsException());

        createdUserReceiver.receive(owner);

        Mockito.verify(createdUserQueueConfig).getQueue();
        Mockito.verify(safeboxService).createSafebox(owner);
    }
}
