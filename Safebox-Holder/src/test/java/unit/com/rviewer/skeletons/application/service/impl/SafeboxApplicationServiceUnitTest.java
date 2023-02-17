package unit.com.rviewer.skeletons.application.service.impl;

import com.rviewer.skeletons.application.model.ItemListDto;
import com.rviewer.skeletons.application.model.SafeboxDto;
import com.rviewer.skeletons.application.service.impl.SafeboxApplicationServiceImpl;
import com.rviewer.skeletons.domain.exception.SafeboxAlreadyExistsException;
import com.rviewer.skeletons.domain.exception.SafeboxDoesNotExistException;
import com.rviewer.skeletons.domain.exception.SafeboxHolderException;
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
import java.util.List;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class SafeboxApplicationServiceUnitTest {

    @InjectMocks
    private SafeboxApplicationServiceImpl safeboxApplicationService;

    @Mock
    private SafeboxService safeboxService;

    @Test
    void getSafeboxUnitTest() {
        String owner = "TEST";
        Safebox safebox = new Safebox();
        safebox.setId("ID");
        safebox.setOwner(owner);
        Mockito.when(safeboxService.getSafebox(owner)).thenReturn(safebox);

        ResponseEntity<SafeboxDto> response = safeboxApplicationService.getSafebox(owner);

        Mockito.verify(safeboxService).getSafebox(owner);

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getStatusCode());
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertNotNull(response.getBody().getId());
        Assertions.assertEquals(safebox.getId(), response.getBody().getId());
    }

    @Test
    void getSafebox_safeboxDoesNotExistException_UnitTest() {
        String owner = "TEST";
        Mockito.when(safeboxService.getSafebox(owner)).thenThrow(new SafeboxDoesNotExistException());

        ResponseEntity<SafeboxDto> response = safeboxApplicationService.getSafebox(owner);

        Mockito.verify(safeboxService).getSafebox(owner);

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getStatusCode());
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertNull(response.getBody());
    }

    @Test
    void getSafebox_errorException_UnitTest() {
        String owner = "TEST";
        Mockito.when(safeboxService.getSafebox(owner)).thenThrow(new SafeboxHolderException());

        ResponseEntity<SafeboxDto> response = safeboxApplicationService.getSafebox(owner);

        Mockito.verify(safeboxService).getSafebox(owner);

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getStatusCode());
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        Assertions.assertNull(response.getBody());
    }


    @Test
    void createSafeboxUnitTest() {
        Safebox safebox = new Safebox();
        safebox.setId(UUID.randomUUID().toString());

        Mockito.when(safeboxService.createSafebox(Mockito.anyString())).thenReturn(safebox);

        ResponseEntity<SafeboxDto> response = safeboxApplicationService.createSafebox("TEST");

        Mockito.verify(safeboxService).createSafebox(Mockito.anyString());

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getStatusCode());
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void createSafebox_alreadyExistingException_UnitTest() {
        Mockito.when(safeboxService.createSafebox(Mockito.anyString())).thenThrow(new SafeboxAlreadyExistsException());

        ResponseEntity<SafeboxDto> response = safeboxApplicationService.createSafebox("TEST");

        Mockito.verify(safeboxService).createSafebox(Mockito.anyString());

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getStatusCode());
        Assertions.assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    void createSafebox_errorException_UnitTest() {
        Mockito.when(safeboxService.createSafebox(Mockito.anyString())).thenThrow(new SafeboxHolderException());

        ResponseEntity<SafeboxDto> response = safeboxApplicationService.createSafebox("TEST");

        Mockito.verify(safeboxService).createSafebox(Mockito.anyString());

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getStatusCode());
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void addItemsToSafeboxUnitTest() {
        ResponseEntity<Void> response =
                safeboxApplicationService.addItemsToSafebox("TEST", Collections.singletonList("TEST"));

        Mockito.verify(safeboxService).addItemsToSafebox(Mockito.anyString(), Mockito.anyList());

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getStatusCode());
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void addItemsToSafebox_safeboxDoesNotExistException_UnitTest() {
        String id = "TEST";
        List<String> itemList = Collections.singletonList("TEST");
        Mockito.doThrow(new SafeboxDoesNotExistException()).when(safeboxService).addItemsToSafebox(Mockito.eq(id), Mockito.anyList());

        ResponseEntity<Void> response =
                safeboxApplicationService.addItemsToSafebox(id, itemList);

        Mockito.verify(safeboxService).addItemsToSafebox(Mockito.anyString(), Mockito.anyList());

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getStatusCode());
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void addItemsToSafebox_errorException_UnitTest() {
        String id = "TEST";
        List<String> itemList = Collections.singletonList("TEST");
        Mockito.doThrow(new SafeboxHolderException()).when(safeboxService).addItemsToSafebox(Mockito.eq(id), Mockito.anyList());

        ResponseEntity<Void> response =
                safeboxApplicationService.addItemsToSafebox(id, itemList);

        Mockito.verify(safeboxService).addItemsToSafebox(Mockito.anyString(), Mockito.anyList());

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getStatusCode());
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void getSafeboxItemsUnitTest() {
        Mockito.when(safeboxService.getSafeboxItems(Mockito.anyString()))
                .thenReturn(Collections.singletonList(new Item("TEST")));

        ResponseEntity<ItemListDto> response = safeboxApplicationService.getSafeboxItems("TEST");

        Mockito.verify(safeboxService).getSafeboxItems(Mockito.anyString());

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getStatusCode());
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getSafeboxItems_safeboxDoesNotExistException_UnitTest() {
        Mockito.when(safeboxService.getSafeboxItems(Mockito.anyString()))
                .thenThrow(new SafeboxDoesNotExistException());

        ResponseEntity<ItemListDto> response = safeboxApplicationService.getSafeboxItems("TEST");

        Mockito.verify(safeboxService).getSafeboxItems(Mockito.anyString());

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getStatusCode());
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getSafeboxItems_errorException_UnitTest() {
        Mockito.when(safeboxService.getSafeboxItems(Mockito.anyString()))
                .thenThrow(new SafeboxHolderException());

        ResponseEntity<ItemListDto> response = safeboxApplicationService.getSafeboxItems("TEST");

        Mockito.verify(safeboxService).getSafeboxItems(Mockito.anyString());

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getStatusCode());
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
