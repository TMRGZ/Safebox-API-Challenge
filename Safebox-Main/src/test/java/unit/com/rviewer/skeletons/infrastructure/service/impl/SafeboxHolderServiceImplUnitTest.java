package unit.com.rviewer.skeletons.infrastructure.service.impl;

import com.rviewer.skeletons.domain.exception.ExternalServiceException;
import com.rviewer.skeletons.domain.exception.SafeboxDoesNotExistException;
import com.rviewer.skeletons.domain.exception.SafeboxMainException;
import com.rviewer.skeletons.domain.model.Item;
import com.rviewer.skeletons.domain.model.Safebox;
import com.rviewer.skeletons.infrastructure.rest.safebox.holder.SafeboxHolderApi;
import com.rviewer.skeletons.infrastructure.rest.safebox.holder.model.HolderItemListDto;
import com.rviewer.skeletons.infrastructure.rest.safebox.holder.model.HolderSafeboxDto;
import com.rviewer.skeletons.infrastructure.service.impl.SafeboxHolderServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class SafeboxHolderServiceImplUnitTest {

    @InjectMocks
    private SafeboxHolderServiceImpl safeboxHolderService;

    @Mock
    private SafeboxHolderApi safeboxHolderApi;

    @Test
    void getSafeboxUnitTest() {
        String id = "TEST";
        Mockito.when(safeboxHolderApi.getSafebox(id)).thenReturn(new HolderSafeboxDto().id(id));

        Safebox safebox = safeboxHolderService.getSafebox(id);

        Mockito.verify(safeboxHolderApi).getSafebox(id);

        Assertions.assertNotNull(safebox);
        Assertions.assertNotNull(safebox.getId());
        Assertions.assertEquals(id, safebox.getId());
    }

    @Test
    void getSafebox_safeboxDoesNotExistException_UnitTest() {
        String id = "TEST";
        Mockito.when(safeboxHolderApi.getSafebox(id)).thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        Assertions.assertThrows(SafeboxDoesNotExistException.class, () -> safeboxHolderService.getSafebox(id));

        Mockito.verify(safeboxHolderApi).getSafebox(id);
    }

    @Test
    void getSafebox_unknownClientErrorException_UnitTest() {
        String id = "TEST";
        Mockito.when(safeboxHolderApi.getSafebox(id)).thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        Assertions.assertThrows(SafeboxMainException.class, () -> safeboxHolderService.getSafebox(id));

        Mockito.verify(safeboxHolderApi).getSafebox(id);
    }

    @Test
    void getSafebox_externalServiceException_UnitTest() {
        String id = "TEST";
        Mockito.when(safeboxHolderApi.getSafebox(id)).thenThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

        Assertions.assertThrows(ExternalServiceException.class, () -> safeboxHolderService.getSafebox(id));

        Mockito.verify(safeboxHolderApi).getSafebox(id);
    }

    @Test
    void getSafeboxItemsUnitTest() {
        String id = "TEST";
        List<String> itemList = Collections.singletonList("ITEM");
        Mockito.when(safeboxHolderApi.getSafeboxItems(id)).thenReturn(new HolderItemListDto().items(itemList));

        List<Item> safeboxItems = safeboxHolderService.getSafeboxItems(id);

        Mockito.verify(safeboxHolderApi).getSafeboxItems(id);

        Assertions.assertNotNull(safeboxItems);
        Assertions.assertEquals(itemList.size(), safeboxItems.size());
        safeboxItems.forEach(item -> Assertions.assertTrue(itemList.contains(item.getDetail())));
    }

    @Test
    void getSafeboxItems_safeboxDoesNotExistException_UnitTest() {
        String id = "TEST";
        Mockito.when(safeboxHolderApi.getSafeboxItems(id)).thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        Assertions.assertThrows(SafeboxDoesNotExistException.class, () -> safeboxHolderService.getSafeboxItems(id));

        Mockito.verify(safeboxHolderApi).getSafeboxItems(id);
    }

    @Test
    void getSafeboxItems_unknownClientErrorException_UnitTest() {
        String id = "TEST";
        Mockito.when(safeboxHolderApi.getSafeboxItems(id)).thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        Assertions.assertThrows(SafeboxMainException.class, () -> safeboxHolderService.getSafeboxItems(id));

        Mockito.verify(safeboxHolderApi).getSafeboxItems(id);
    }

    @Test
    void getSafeboxItems_externalServiceException_UnitTest() {
        String id = "TEST";
        Mockito.when(safeboxHolderApi.getSafeboxItems(id)).thenThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

        Assertions.assertThrows(ExternalServiceException.class, () -> safeboxHolderService.getSafeboxItems(id));

        Mockito.verify(safeboxHolderApi).getSafeboxItems(id);
    }

    @Test
    void putSafeboxItemsUnitTest() {
        String id = "TEST";
        List<Item> itemList = Collections.singletonList(new Item("ITEM"));
        ArgumentCaptor<HolderItemListDto> itemListDtoArgumentCaptor = ArgumentCaptor.forClass(HolderItemListDto.class);

        safeboxHolderService.putSafeboxItems(id, itemList);

        Mockito.verify(safeboxHolderApi).putSafeboxItems(Mockito.eq(id), itemListDtoArgumentCaptor.capture());

        HolderItemListDto itemListDto = itemListDtoArgumentCaptor.getValue();

        Assertions.assertNotNull(itemListDto);
        Assertions.assertNotNull(itemListDto.getItems());
        Assertions.assertEquals(itemList.size(), itemListDto.getItems().size());
        itemList.forEach(item -> Assertions.assertTrue(itemListDto.getItems().contains(item.getDetail())));
    }

    @Test
    void putSafeboxItems_safeboxDoesNotExistException_UnitTest() {
        String id = "TEST";
        List<Item> itemList = Collections.singletonList(new Item("ITEM"));
        Mockito.doThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND)).when(safeboxHolderApi).putSafeboxItems(Mockito.eq(id), Mockito.any());

        Assertions.assertThrows(SafeboxDoesNotExistException.class, () -> safeboxHolderService.putSafeboxItems(id, itemList));

        Mockito.verify(safeboxHolderApi).putSafeboxItems(Mockito.eq(id), Mockito.any());
    }

    @Test
    void putSafeboxItems_unknownClientErrorException_UnitTest() {
        String id = "TEST";
        List<Item> itemList = Collections.singletonList(new Item("ITEM"));
        Mockito.doThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST)).when(safeboxHolderApi).putSafeboxItems(Mockito.eq(id), Mockito.any());

        Assertions.assertThrows(SafeboxMainException.class, () -> safeboxHolderService.putSafeboxItems(id, itemList));

        Mockito.verify(safeboxHolderApi).putSafeboxItems(Mockito.eq(id), Mockito.any());
    }

    @Test
    void putSafeboxItems_externalServiceException_UnitTest() {
        String id = "TEST";
        List<Item> itemList = Collections.singletonList(new Item("ITEM"));
        Mockito.doThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR)).when(safeboxHolderApi).putSafeboxItems(Mockito.eq(id), Mockito.any());

        Assertions.assertThrows(ExternalServiceException.class, () -> safeboxHolderService.putSafeboxItems(id, itemList));

        Mockito.verify(safeboxHolderApi).putSafeboxItems(Mockito.eq(id), Mockito.any());
    }
}
