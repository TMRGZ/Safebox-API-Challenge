package unit.com.rviewer.skeletons.infrastructure.mapper.domain;

import com.rviewer.skeletons.domain.model.Safebox;
import com.rviewer.skeletons.infrastructure.mapper.domain.SafeboxMapper;
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
class SafeboxMapperUnitTest {

    @InjectMocks
    private SafeboxMapper safeboxMapper;

    @Mock
    private ModelMapper modelMapper;

    @Test
    void mapUnitTest() {
        safeboxMapper.map(new Safebox());

        Mockito.verify(modelMapper).map(Mockito.any(), Mockito.any());
    }

    @Test
    void mapListUnitTest() {
        List<Safebox> safeboxList = Collections.emptyList();

        safeboxMapper.map(safeboxList);

        Mockito.verify(modelMapper, Mockito.times(safeboxList.size())).map(Mockito.any(), Mockito.any());
    }
}
