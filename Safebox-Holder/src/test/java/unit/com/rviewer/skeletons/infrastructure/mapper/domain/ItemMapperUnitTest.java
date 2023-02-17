package unit.com.rviewer.skeletons.infrastructure.mapper.domain;

import com.rviewer.skeletons.domain.model.Item;
import com.rviewer.skeletons.infrastructure.mapper.domain.ItemMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class ItemMapperUnitTest {

    @InjectMocks
    private ItemMapper itemMapper;

    @Mock
    private ModelMapper modelMapper;

    @Test
    void mapUnitTest() {
        itemMapper.map(new Item());

        Mockito.verify(modelMapper).map(Mockito.any(), Mockito.any());
    }

    @Test
    void mapListUnitTest() {
        List<Item> itemList = Collections.emptyList();

        itemMapper.map(itemList);

        Mockito.verify(modelMapper, Mockito.times(itemList.size())).map(Mockito.any(), Mockito.any());
    }
}
