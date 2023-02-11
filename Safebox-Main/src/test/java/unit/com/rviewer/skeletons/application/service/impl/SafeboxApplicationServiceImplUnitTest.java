package unit.com.rviewer.skeletons.application.service.impl;

import com.rviewer.skeletons.application.model.CreateSafeboxRequestDto;
import com.rviewer.skeletons.application.model.CreatedSafeboxDto;
import com.rviewer.skeletons.application.model.ItemListDto;
import com.rviewer.skeletons.application.model.SafeboxKeyDto;
import com.rviewer.skeletons.application.service.impl.SafeboxApplicationServiceImpl;
import com.rviewer.skeletons.domain.model.Item;
import com.rviewer.skeletons.domain.service.SafeboxService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class SafeboxApplicationServiceImplUnitTest {

    @InjectMocks
    private SafeboxApplicationServiceImpl safeboxApplicationService;

    @Mock
    private SafeboxService safeboxService;

    @Test
    void createSafeboxUnitTest() {
        CreateSafeboxRequestDto createSafeboxRequestDto = new CreateSafeboxRequestDto();
        Mockito.when(safeboxService.createSafebox(Mockito.any())).thenReturn("TEST");

        ResponseEntity<CreatedSafeboxDto> response = safeboxApplicationService.createSafebox(createSafeboxRequestDto);

        Mockito.verify(safeboxService).createSafebox(Mockito.any());

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getStatusCode());
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertNotNull(response.getBody().getId());
    }

    @Test
    void openSafeboxUnitTest() {
        String id = "TEST";
        Mockito.when(safeboxService.openSafebox(id)).thenReturn("TOKEN");

        ResponseEntity<SafeboxKeyDto> response = safeboxApplicationService.openSafebox(id);

        Mockito.verify(safeboxService).openSafebox(id);

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getStatusCode());
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertNotNull(response.getBody().getToken());
    }

    @Test
    void getSafeboxItemsUnitTest() {
        String id = "TEST";
        Item item = new Item("ITEM");
        List<Item> itemList = Collections.singletonList(item);
        Mockito.when(safeboxService.getSafeboxItems(id)).thenReturn(itemList);

        ResponseEntity<ItemListDto> response = safeboxApplicationService.getSafeboxItems(id);

        Mockito.verify(safeboxService).getSafeboxItems(id);

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getStatusCode());
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertNotNull(response.getBody().getItems());
        Assertions.assertEquals(itemList.size(), response.getBody().getItems().size());
    }

    @Test
    void addItemsToSafeboxUnitTest() {
        String id = "TEST";
        List<String> itemList = Collections.singletonList("ITEM");
        ItemListDto itemListDto = new ItemListDto().items(itemList);
        ArgumentCaptor<List<Item>> itemListCaptor = ArgumentCaptor.forClass(List.class);

        ResponseEntity<Void> response = safeboxApplicationService.addItemsToSafebox(id, itemListDto);

        Mockito.verify(safeboxService).addItemsToSafebox(Mockito.eq(id), itemListCaptor.capture());

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getStatusCode());
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNull(response.getBody());

        List<Item> capturedList = itemListCaptor.getValue();

        Assertions.assertEquals(itemList.size(), capturedList.size());
        capturedList.forEach(item -> Assertions.assertTrue(itemList.contains(item.getDetail())));
    }
}