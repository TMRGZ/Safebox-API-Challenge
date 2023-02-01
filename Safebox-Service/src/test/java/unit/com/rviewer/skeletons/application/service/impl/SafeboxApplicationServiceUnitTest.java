package unit.com.rviewer.skeletons.application.service.impl;

import com.rviewer.skeletons.application.model.ItemListDto;
import com.rviewer.skeletons.application.model.SafeboxDto;
import com.rviewer.skeletons.application.service.impl.SafeboxApplicationServiceImpl;
import com.rviewer.skeletons.domain.model.Item;
import com.rviewer.skeletons.domain.model.Safebox;
import com.rviewer.skeletons.domain.service.SafeboxService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class SafeboxApplicationServiceUnitTest {

    @InjectMocks
    private SafeboxApplicationServiceImpl safeboxApplicationService;

    @Mock
    private SafeboxService safeboxService;

    @Test
    void createSafeboxUnitTest() {
        Safebox safebox = new Safebox();
        safebox.setId(UUID.randomUUID());

        Mockito.when(safeboxService.createSafebox(Mockito.anyString())).thenReturn(safebox);

        ResponseEntity<SafeboxDto> response = safeboxApplicationService.createSafebox("TEST");

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getStatusCode());
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());

        Mockito.verify(safeboxService).createSafebox(Mockito.anyString());
    }

    @Test
    void addItemsToSafeboxUnitTest() {
        ResponseEntity<Void> response =
                safeboxApplicationService.addItemsToSafebox("TEST", Collections.singletonList("TEST"));

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getStatusCode());
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        Mockito.verify(safeboxService).addItemsToSafebox(Mockito.anyString(), Mockito.anyList());
    }

    @Test
    void getSafeboxItemsUnitTest() {
        Mockito.when(safeboxService.getSafeboxItems(Mockito.anyString()))
                .thenReturn(Collections.singletonList(new Item("TEST")));

        ResponseEntity<ItemListDto> response = safeboxApplicationService.getSafeboxItems("TEST");

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getStatusCode());
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        Mockito.verify(safeboxService).getSafeboxItems(Mockito.anyString());
    }
}
