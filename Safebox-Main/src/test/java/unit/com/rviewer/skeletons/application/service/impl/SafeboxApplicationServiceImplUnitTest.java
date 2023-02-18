package unit.com.rviewer.skeletons.application.service.impl;

import com.rviewer.skeletons.application.mapper.InputItemMapper;
import com.rviewer.skeletons.application.mapper.InputUserMapper;
import com.rviewer.skeletons.application.model.CreateSafeboxRequestDto;
import com.rviewer.skeletons.application.model.CreatedSafeboxDto;
import com.rviewer.skeletons.application.model.ItemListDto;
import com.rviewer.skeletons.application.model.SafeboxKeyDto;
import com.rviewer.skeletons.application.service.impl.SafeboxApplicationServiceImpl;
import com.rviewer.skeletons.domain.exception.ExternalServiceException;
import com.rviewer.skeletons.domain.exception.SafeboxAlreadyExistsException;
import com.rviewer.skeletons.domain.exception.SafeboxDoesNotExistException;
import com.rviewer.skeletons.domain.exception.SafeboxMainException;
import com.rviewer.skeletons.domain.model.Item;
import com.rviewer.skeletons.domain.model.User;
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

@ExtendWith(MockitoExtension.class)
class SafeboxApplicationServiceImplUnitTest {

    @InjectMocks
    private SafeboxApplicationServiceImpl safeboxApplicationService;

    @Mock
    private SafeboxService safeboxService;

    @Mock
    private InputUserMapper userMapper;

    @Mock
    private InputItemMapper itemMapper;

    @Test
    void createSafeboxUnitTest() {
        CreateSafeboxRequestDto createSafeboxRequestDto = new CreateSafeboxRequestDto();
        User user = new User();
        Mockito.when(userMapper.map(createSafeboxRequestDto)).thenReturn(user);
        Mockito.when(safeboxService.createSafebox(Mockito.any())).thenReturn("TEST");

        ResponseEntity<CreatedSafeboxDto> response = safeboxApplicationService.createSafebox(createSafeboxRequestDto);

        Mockito.verify(userMapper).map(createSafeboxRequestDto);
        Mockito.verify(safeboxService).createSafebox(Mockito.any());

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getStatusCode());
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertNotNull(response.getBody().getId());
    }

    @Test
    void createSafebox_alreadyExistingException_UnitTest() {
        CreateSafeboxRequestDto createSafeboxRequestDto = new CreateSafeboxRequestDto();
        User user = new User();
        Mockito.when(userMapper.map(createSafeboxRequestDto)).thenReturn(user);
        Mockito.when(safeboxService.createSafebox(Mockito.any())).thenThrow(new SafeboxAlreadyExistsException());

        ResponseEntity<CreatedSafeboxDto> response = safeboxApplicationService.createSafebox(createSafeboxRequestDto);

        Mockito.verify(userMapper).map(createSafeboxRequestDto);
        Mockito.verify(safeboxService).createSafebox(Mockito.any());

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getStatusCode());
        Assertions.assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        Assertions.assertNull(response.getBody());
    }

    @Test
    void createSafebox_externalServiceException_UnitTest() {
        CreateSafeboxRequestDto createSafeboxRequestDto = new CreateSafeboxRequestDto();
        User user = new User();
        Mockito.when(userMapper.map(createSafeboxRequestDto)).thenReturn(user);
        Mockito.when(safeboxService.createSafebox(Mockito.any())).thenThrow(new ExternalServiceException());

        ResponseEntity<CreatedSafeboxDto> response = safeboxApplicationService.createSafebox(createSafeboxRequestDto);

        Mockito.verify(userMapper).map(createSafeboxRequestDto);
        Mockito.verify(safeboxService).createSafebox(Mockito.any());

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getStatusCode());
        Assertions.assertEquals(HttpStatus.BAD_GATEWAY, response.getStatusCode());
        Assertions.assertNull(response.getBody());
    }

    @Test
    void createSafebox_errorException_UnitTest() {
        CreateSafeboxRequestDto createSafeboxRequestDto = new CreateSafeboxRequestDto();
        User user = new User();
        Mockito.when(userMapper.map(createSafeboxRequestDto)).thenReturn(user);
        Mockito.when(safeboxService.createSafebox(Mockito.any())).thenThrow(new SafeboxMainException());

        ResponseEntity<CreatedSafeboxDto> response = safeboxApplicationService.createSafebox(createSafeboxRequestDto);

        Mockito.verify(userMapper).map(createSafeboxRequestDto);
        Mockito.verify(safeboxService).createSafebox(Mockito.any());

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getStatusCode());
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        Assertions.assertNull(response.getBody());
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
    void openSafebox_safeboxDoesNotExistException_UnitTest() {
        String id = "TEST";
        Mockito.when(safeboxService.openSafebox(id)).thenThrow(new SafeboxDoesNotExistException());

        ResponseEntity<SafeboxKeyDto> response = safeboxApplicationService.openSafebox(id);

        Mockito.verify(safeboxService).openSafebox(id);

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getStatusCode());
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertNull(response.getBody());
    }

    @Test
    void openSafebox_externalServiceException_UnitTest() {
        String id = "TEST";
        Mockito.when(safeboxService.openSafebox(id)).thenThrow(new ExternalServiceException());

        ResponseEntity<SafeboxKeyDto> response = safeboxApplicationService.openSafebox(id);

        Mockito.verify(safeboxService).openSafebox(id);

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getStatusCode());
        Assertions.assertEquals(HttpStatus.BAD_GATEWAY, response.getStatusCode());
        Assertions.assertNull(response.getBody());
    }

    @Test
    void openSafebox_errorException_UnitTest() {
        String id = "TEST";
        Mockito.when(safeboxService.openSafebox(id)).thenThrow(new SafeboxMainException());

        ResponseEntity<SafeboxKeyDto> response = safeboxApplicationService.openSafebox(id);

        Mockito.verify(safeboxService).openSafebox(id);

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getStatusCode());
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        Assertions.assertNull(response.getBody());
    }

    @Test
    void getSafeboxItemsUnitTest() {
        String id = "TEST";
        Item item = new Item();
        item.setDetail("ITEM");
        List<Item> itemList = Collections.singletonList(item);
        Mockito.when(safeboxService.getSafeboxItems(id)).thenReturn(itemList);

        ResponseEntity<ItemListDto> response = safeboxApplicationService.getSafeboxItems(id);

        Mockito.verify(safeboxService).getSafeboxItems(id);
        Mockito.verify(itemMapper).map(itemList);

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getStatusCode());
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getSafeboxItems_safeboxDoesNotExistException_UnitTest() {
        String id = "TEST";
        Mockito.when(safeboxService.getSafeboxItems(id)).thenThrow(new SafeboxDoesNotExistException());

        ResponseEntity<ItemListDto> response = safeboxApplicationService.getSafeboxItems(id);

        Mockito.verify(safeboxService).getSafeboxItems(id);

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getStatusCode());
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertNull(response.getBody());
    }

    @Test
    void getSafeboxItems_externalServiceException_UnitTest() {
        String id = "TEST";
        Mockito.when(safeboxService.getSafeboxItems(id)).thenThrow(new ExternalServiceException());

        ResponseEntity<ItemListDto> response = safeboxApplicationService.getSafeboxItems(id);

        Mockito.verify(safeboxService).getSafeboxItems(id);

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getStatusCode());
        Assertions.assertEquals(HttpStatus.BAD_GATEWAY, response.getStatusCode());
        Assertions.assertNull(response.getBody());
    }

    @Test
    void getSafeboxItems_errorException_UnitTest() {
        String id = "TEST";
        Mockito.when(safeboxService.getSafeboxItems(id)).thenThrow(new SafeboxMainException());

        ResponseEntity<ItemListDto> response = safeboxApplicationService.getSafeboxItems(id);

        Mockito.verify(safeboxService).getSafeboxItems(id);

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getStatusCode());
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        Assertions.assertNull(response.getBody());
    }

    @Test
    void addItemsToSafeboxUnitTest() {
        String id = "TEST";
        List<String> itemList = Collections.singletonList("ITEM");
        ItemListDto itemListDto = new ItemListDto().items(itemList);
        List<Item> items = Collections.singletonList(new Item());
        Mockito.when(itemMapper.map(itemListDto)).thenReturn(items);

        ResponseEntity<Void> response = safeboxApplicationService.addItemsToSafebox(id, itemListDto);

        Mockito.verify(itemMapper).map(itemListDto);
        Mockito.verify(safeboxService).addItemsToSafebox(Mockito.eq(id), Mockito.anyList());

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getStatusCode());
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNull(response.getBody());
    }

    @Test
    void addItemsToSafebox_safeboxDoesNotExistException_UnitTest() {
        String id = "TEST";
        List<String> itemList = Collections.singletonList("ITEM");
        ItemListDto itemListDto = new ItemListDto().items(itemList);
        List<Item> items = Collections.singletonList(new Item());
        Mockito.when(itemMapper.map(itemListDto)).thenReturn(items);
        Mockito.doThrow(new SafeboxDoesNotExistException()).when(safeboxService).addItemsToSafebox(id, items);

        ResponseEntity<Void> response = safeboxApplicationService.addItemsToSafebox(id, itemListDto);

        Mockito.verify(safeboxService).addItemsToSafebox(Mockito.eq(id), Mockito.anyList());

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getStatusCode());
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertNull(response.getBody());
    }

    @Test
    void addItemsToSafebox_externalServiceException_UnitTest() {
        String id = "TEST";
        List<String> itemList = Collections.singletonList("ITEM");
        ItemListDto itemListDto = new ItemListDto().items(itemList);
        List<Item> items = Collections.singletonList(new Item());
        Mockito.when(itemMapper.map(itemListDto)).thenReturn(items);
        Mockito.doThrow(new ExternalServiceException()).when(safeboxService).addItemsToSafebox(id, items);

        ResponseEntity<Void> response = safeboxApplicationService.addItemsToSafebox(id, itemListDto);

        Mockito.verify(safeboxService).addItemsToSafebox(id, items);

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getStatusCode());
        Assertions.assertEquals(HttpStatus.BAD_GATEWAY, response.getStatusCode());
        Assertions.assertNull(response.getBody());
    }

    @Test
    void addItemsToSafebox_errorException_UnitTest() {
        String id = "TEST";
        List<String> itemList = Collections.singletonList("ITEM");
        ItemListDto itemListDto = new ItemListDto().items(itemList);
        List<Item> items = Collections.singletonList(new Item());
        Mockito.when(itemMapper.map(itemListDto)).thenReturn(items);
        Mockito.doThrow(new SafeboxMainException()).when(safeboxService).addItemsToSafebox(id, items);

        ResponseEntity<Void> response = safeboxApplicationService.addItemsToSafebox(id, itemListDto);

        Mockito.verify(safeboxService).addItemsToSafebox(id, items);

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getStatusCode());
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        Assertions.assertNull(response.getBody());
    }
}
