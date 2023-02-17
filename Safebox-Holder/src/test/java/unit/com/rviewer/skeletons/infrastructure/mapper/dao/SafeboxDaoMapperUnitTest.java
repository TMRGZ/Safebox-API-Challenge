package unit.com.rviewer.skeletons.infrastructure.mapper.dao;

import com.rviewer.skeletons.infrastructure.mapper.dao.SafeboxDaoMapper;
import com.rviewer.skeletons.infrastructure.persistence.dao.SafeboxDao;
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
class SafeboxDaoMapperUnitTest {

    @InjectMocks
    private SafeboxDaoMapper safeboxDaoMapper;

    @Mock
    private ModelMapper modelMapper;

    @Test
    void mapUnitTest() {
        safeboxDaoMapper.map(new SafeboxDao());

        Mockito.verify(modelMapper).map(Mockito.any(), Mockito.any());
    }

    @Test
    void mapListUnitTest() {
        List<SafeboxDao> safeboxDaoList = Collections.emptyList();

        safeboxDaoMapper.map(safeboxDaoList);

        Mockito.verify(modelMapper, Mockito.times(safeboxDaoList.size())).map(Mockito.any(), Mockito.any());
    }
}
