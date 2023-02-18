package unit.com.rviewer.skeletons.application.mapper;

import com.rviewer.skeletons.application.mapper.InputItemMapper;
import com.rviewer.skeletons.application.model.ItemListDto;
import com.rviewer.skeletons.domain.model.Item;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class InputItemMapperUnitTest {

    @InjectMocks
    private InputItemMapper itemMapper;

    @Test
    void mapItemUnitTest() {
        String detail = "TEST";

        Item item = itemMapper.map(detail);

        Assertions.assertNotNull(item);
        Assertions.assertEquals(detail, item.getDetail());
    }

    @Test
    void mapItemListDtoUnitTest() {
        ItemListDto itemListDto = new ItemListDto().items(Collections.singletonList("TEST"));

        List<Item> itemList = itemMapper.map(itemListDto);

        Assertions.assertNotNull(itemList);
        Assertions.assertFalse(itemList.isEmpty());
        Assertions.assertEquals(itemListDto.getItems().size(), itemList.size());
    }

    @Test
    void mapItemListUnitTest() {
        List<Item> itemList = Collections.singletonList(new Item());

        ItemListDto itemListDto = itemMapper.map(itemList);

        Assertions.assertNotNull(itemListDto);
        Assertions.assertNotNull(itemListDto.getItems());
        Assertions.assertFalse(itemListDto.getItems().isEmpty());
        Assertions.assertEquals(itemList.size(), itemListDto.getItems().size());
    }
}
