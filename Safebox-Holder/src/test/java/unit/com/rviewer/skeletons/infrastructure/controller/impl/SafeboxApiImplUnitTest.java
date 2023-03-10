package unit.com.rviewer.skeletons.infrastructure.controller.impl;

import com.rviewer.skeletons.application.model.CreateSafeboxRequestDto;
import com.rviewer.skeletons.application.model.ItemListDto;
import com.rviewer.skeletons.application.service.SafeboxApplicationService;
import com.rviewer.skeletons.infrastructure.controller.impl.SafeboxApiImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

@ExtendWith(MockitoExtension.class)
class SafeboxApiImplUnitTest {

    @InjectMocks
    private SafeboxApiImpl safeboxApi;

    @Mock
    private SafeboxApplicationService safeboxApplicationService;

    @Test
    void getSafeboxItemsUnitTest() {
        safeboxApi.getSafeboxItems("TEST");

        Mockito.verify(safeboxApplicationService).getSafeboxItems(Mockito.anyString());
    }

    @Test
    void putSafeboxItemsUnitTest() {
        safeboxApi.putSafeboxItems("TEST", new ItemListDto().items(Collections.emptyList()));

        Mockito.verify(safeboxApplicationService).addItemsToSafebox(Mockito.anyString(), Mockito.any());
    }

    @Test
    void getSafeboxUnitTest() {
        String id = "TEST";

        safeboxApi.getSafebox(id);

        Mockito.verify(safeboxApplicationService).getSafebox(id);
    }

    @Test
    void postSafeboxtUnitTest() {
        safeboxApi.postSafebox(new CreateSafeboxRequestDto().owner("TEST"));

        Mockito.verify(safeboxApplicationService).createSafebox(Mockito.any());
    }
}
