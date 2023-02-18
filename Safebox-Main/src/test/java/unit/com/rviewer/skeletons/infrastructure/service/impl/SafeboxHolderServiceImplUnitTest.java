package unit.com.rviewer.skeletons.infrastructure.service.impl;

import com.rviewer.skeletons.domain.exception.ExternalServiceException;
import com.rviewer.skeletons.domain.exception.SafeboxDoesNotExistException;
import com.rviewer.skeletons.domain.exception.SafeboxMainException;
import com.rviewer.skeletons.domain.model.Item;
import com.rviewer.skeletons.domain.model.Safebox;
import com.rviewer.skeletons.infrastructure.mapper.ItemMapper;
import com.rviewer.skeletons.infrastructure.mapper.SafeboxMapper;
import com.rviewer.skeletons.infrastructure.rest.safebox.holder.SafeboxHolderApi;
import com.rviewer.skeletons.infrastructure.rest.safebox.holder.model.HolderItemListDto;
import com.rviewer.skeletons.infrastructure.rest.safebox.holder.model.HolderSafeboxDto;
import com.rviewer.skeletons.infrastructure.service.impl.SafeboxHolderServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

    @Mock
    private SafeboxMapper safeboxMapper;

    @Mock
    private ItemMapper itemMapper;

    @Test
    void getSafeboxUnitTest() {
        String id = "TEST";
        HolderSafeboxDto safeboxDto = new HolderSafeboxDto().id(id);
        Mockito.when(safeboxHolderApi.getSafebox(id)).thenReturn(safeboxDto);
        Mockito.when(safeboxMapper.map(safeboxDto)).thenReturn(new Safebox());

        safeboxHolderService.getSafebox(id);

        Mockito.verify(safeboxHolderApi).getSafebox(id);
        Mockito.verify(safeboxMapper).map(safeboxDto);
    }

    @Test
    void getSafebox_safeboxDoesNotExistException_UnitTest() {
        String id = "TEST";
        Mockito.when(safeboxHolderApi.getSafebox(id)).thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        Assertions.assertThrows(SafeboxDoesNotExistException.class, () -> safeboxHolderService.getSafebox(id));

        Mockito.verify(safeboxHolderApi).getSafebox(id);
        Mockito.verify(safeboxMapper, Mockito.never()).map(Mockito.any());
    }

    @Test
    void getSafebox_unknownClientErrorException_UnitTest() {
        String id = "TEST";
        Mockito.when(safeboxHolderApi.getSafebox(id)).thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        Assertions.assertThrows(SafeboxMainException.class, () -> safeboxHolderService.getSafebox(id));

        Mockito.verify(safeboxHolderApi).getSafebox(id);
        Mockito.verify(safeboxMapper, Mockito.never()).map(Mockito.any());
    }

    @Test
    void getSafebox_externalServiceException_UnitTest() {
        String id = "TEST";
        Mockito.when(safeboxHolderApi.getSafebox(id)).thenThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

        Assertions.assertThrows(ExternalServiceException.class, () -> safeboxHolderService.getSafebox(id));

        Mockito.verify(safeboxHolderApi).getSafebox(id);
        Mockito.verify(safeboxMapper, Mockito.never()).map(Mockito.any());
    }

    @Test
    void getSafeboxItemsUnitTest() {
        String id = "TEST";
        List<String> itemList = Collections.singletonList("ITEM");
        HolderItemListDto itemListDto = new HolderItemListDto().items(itemList);
        Mockito.when(safeboxHolderApi.getSafeboxItems(id)).thenReturn(itemListDto);

        safeboxHolderService.getSafeboxItems(id);

        Mockito.verify(safeboxHolderApi).getSafeboxItems(id);
        Mockito.verify(itemMapper).map(itemListDto);
    }

    @Test
    void getSafeboxItems_safeboxDoesNotExistException_UnitTest() {
        String id = "TEST";
        Mockito.when(safeboxHolderApi.getSafeboxItems(id)).thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        Assertions.assertThrows(SafeboxDoesNotExistException.class, () -> safeboxHolderService.getSafeboxItems(id));

        Mockito.verify(safeboxHolderApi).getSafeboxItems(id);
        Mockito.verify(itemMapper, Mockito.never()).map(Mockito.any(HolderItemListDto.class));
    }

    @Test
    void getSafeboxItems_unknownClientErrorException_UnitTest() {
        String id = "TEST";
        Mockito.when(safeboxHolderApi.getSafeboxItems(id)).thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        Assertions.assertThrows(SafeboxMainException.class, () -> safeboxHolderService.getSafeboxItems(id));

        Mockito.verify(safeboxHolderApi).getSafeboxItems(id);
        Mockito.verify(itemMapper, Mockito.never()).map(Mockito.any(HolderItemListDto.class));
    }

    @Test
    void getSafeboxItems_externalServiceException_UnitTest() {
        String id = "TEST";
        Mockito.when(safeboxHolderApi.getSafeboxItems(id)).thenThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

        Assertions.assertThrows(ExternalServiceException.class, () -> safeboxHolderService.getSafeboxItems(id));

        Mockito.verify(safeboxHolderApi).getSafeboxItems(id);
        Mockito.verify(itemMapper, Mockito.never()).map(Mockito.any(HolderItemListDto.class));
    }

    @Test
    void putSafeboxItemsUnitTest() {
        String id = "TEST";
        Item item = new Item();
        item.setDetail("ITEM");
        List<Item> itemList = Collections.singletonList(item);

        safeboxHolderService.putSafeboxItems(id, itemList);

        Mockito.verify(itemMapper).map(itemList);
        Mockito.verify(safeboxHolderApi).putSafeboxItems(Mockito.eq(id), Mockito.any());
    }

    @Test
    void putSafeboxItems_safeboxDoesNotExistException_UnitTest() {
        String id = "TEST";
        Item item = new Item();
        item.setDetail("ITEM");
        List<Item> itemList = Collections.singletonList(item);
        Mockito.doThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND)).when(safeboxHolderApi).putSafeboxItems(Mockito.eq(id), Mockito.any());

        Assertions.assertThrows(SafeboxDoesNotExistException.class, () -> safeboxHolderService.putSafeboxItems(id, itemList));

        Mockito.verify(itemMapper).map(itemList);
        Mockito.verify(safeboxHolderApi).putSafeboxItems(Mockito.eq(id), Mockito.any());
    }

    @Test
    void putSafeboxItems_unknownClientErrorException_UnitTest() {
        String id = "TEST";
        Item item = new Item();
        item.setDetail("ITEM");
        List<Item> itemList = Collections.singletonList(item);
        Mockito.doThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST)).when(safeboxHolderApi).putSafeboxItems(Mockito.eq(id), Mockito.any());

        Assertions.assertThrows(SafeboxMainException.class, () -> safeboxHolderService.putSafeboxItems(id, itemList));

        Mockito.verify(itemMapper).map(itemList);
        Mockito.verify(safeboxHolderApi).putSafeboxItems(Mockito.eq(id), Mockito.any());
    }

    @Test
    void putSafeboxItems_externalServiceException_UnitTest() {
        String id = "TEST";
        Item item = new Item();
        item.setDetail("ITEM");
        List<Item> itemList = Collections.singletonList(item);
        Mockito.doThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR)).when(safeboxHolderApi).putSafeboxItems(Mockito.eq(id), Mockito.any());

        Assertions.assertThrows(ExternalServiceException.class, () -> safeboxHolderService.putSafeboxItems(id, itemList));

        Mockito.verify(itemMapper).map(itemList);
        Mockito.verify(safeboxHolderApi).putSafeboxItems(Mockito.eq(id), Mockito.any());
    }
}
