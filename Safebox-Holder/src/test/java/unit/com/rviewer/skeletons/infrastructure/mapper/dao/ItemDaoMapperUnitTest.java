package unit.com.rviewer.skeletons.infrastructure.mapper.dao;

import com.rviewer.skeletons.infrastructure.mapper.dao.ItemDaoMapper;
import com.rviewer.skeletons.infrastructure.persistence.dao.ItemDao;
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
class ItemDaoMapperUnitTest {

    @InjectMocks
    private ItemDaoMapper itemDaoMapper;

    @Mock
    private ModelMapper modelMapper;

    @Test
    void mapUnitTest() {
        itemDaoMapper.map(new ItemDao());

        Mockito.verify(modelMapper).map(Mockito.any(), Mockito.any());
    }

    @Test
    void mapListUnitTest() {
        List<ItemDao> itemDaoList = Collections.emptyList();

        itemDaoMapper.map(itemDaoList);

        Mockito.verify(modelMapper, Mockito.times(itemDaoList.size())).map(Mockito.any(), Mockito.any());
    }
}
