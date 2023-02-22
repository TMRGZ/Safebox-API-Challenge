package unit.com.rviewer.skeletons.infrastructure.mapper;

import com.rviewer.skeletons.domain.model.Item;
import com.rviewer.skeletons.infrastructure.mapper.ItemMapper;
import com.rviewer.skeletons.infrastructure.rest.safebox.holder.model.HolderItemListDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class ItemMapperUnitTest {

    @InjectMocks
    private ItemMapper itemMapper;

    @Test
    void mapItemUnitTest() {
        String detail = "TEST";

        Item item = itemMapper.map(detail);

        Assertions.assertNotNull(item);
        Assertions.assertNotNull(item.getDetail());
        Assertions.assertEquals(detail, item.getDetail());
    }

    @Test
    void mapItemListDtoUnitTest() {
        HolderItemListDto itemListDto = new HolderItemListDto().items(Collections.singletonList("TEST"));

        List<Item> itemList = itemMapper.map(itemListDto);

        Assertions.assertNotNull(itemList);
        Assertions.assertFalse(itemList.isEmpty());
        Assertions.assertEquals(itemListDto.getItems().size(), itemList.size());
    }

    @Test
    void mapItemListUnitTest() {
        List<Item> itemList = Collections.singletonList(new Item());

        HolderItemListDto itemListDto = itemMapper.map(itemList);

        Assertions.assertNotNull(itemListDto);
        Assertions.assertNotNull(itemListDto.getItems());
        Assertions.assertFalse(itemListDto.getItems().isEmpty());
        Assertions.assertEquals(itemList.size(), itemListDto.getItems().size());
    }
}
