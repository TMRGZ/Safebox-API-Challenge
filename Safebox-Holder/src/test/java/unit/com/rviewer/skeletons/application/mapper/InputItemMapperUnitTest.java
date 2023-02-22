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
    private InputItemMapper inputItemMapper;

    @Test
    void mapDtoUnitTest() {
        List<String> stringList = Collections.singletonList("TEST");
        ItemListDto itemListDto = new ItemListDto().items(stringList);

        List<Item> itemList = inputItemMapper.map(itemListDto);

        Assertions.assertNotNull(itemList);
        Assertions.assertEquals(stringList.size(), itemList.size());
    }

    @Test
    void mapItemList() {
        Item item = new Item();
        item.setDetail("TEST");
        List<Item> itemList = Collections.singletonList(item);

        ItemListDto itemListDto = inputItemMapper.map(itemList);

        Assertions.assertNotNull(itemListDto);
        Assertions.assertEquals(itemList.size(), itemListDto.getItems().size());
    }
}
