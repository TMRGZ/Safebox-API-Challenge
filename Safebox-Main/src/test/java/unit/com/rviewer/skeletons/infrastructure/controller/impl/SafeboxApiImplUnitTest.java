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

@ExtendWith(MockitoExtension.class)
class SafeboxApiImplUnitTest {

    @InjectMocks
    private SafeboxApiImpl safeboxApi;

    @Mock
    private SafeboxApplicationService safeboxApplicationService;

    @Test
    void getSafeboxItemsUnitTest() {
        String id = "TEST";

        safeboxApi.getSafeboxItems(id);

        Mockito.verify(safeboxApplicationService).getSafeboxItems(id);
    }

    @Test
    void openSafeboxUnitTest() {
        String id = "TEST";

        safeboxApi.openSafebox(id);

        Mockito.verify(safeboxApplicationService).openSafebox(id);
    }

    @Test
    void postSafeboxUnitTest() {
        CreateSafeboxRequestDto createSafeboxRequestDto = new CreateSafeboxRequestDto();
        createSafeboxRequestDto.setName("TEST");
        createSafeboxRequestDto.setPassword("TEST");

        safeboxApi.postSafebox(createSafeboxRequestDto);

        Mockito.verify(safeboxApplicationService).createSafebox(createSafeboxRequestDto);
    }

    @Test
    void putSafeboxItemsUnitTest() {
        String id = "TEST";
        ItemListDto itemListDto = new ItemListDto();

        safeboxApi.putSafeboxItems(id, itemListDto);

        Mockito.verify(safeboxApplicationService).addItemsToSafebox(id, itemListDto);
    }
}
