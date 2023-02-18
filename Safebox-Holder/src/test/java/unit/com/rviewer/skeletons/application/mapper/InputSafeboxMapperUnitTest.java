package unit.com.rviewer.skeletons.application.mapper;

import com.rviewer.skeletons.application.mapper.InputSafeboxMapper;
import com.rviewer.skeletons.application.model.SafeboxDto;
import com.rviewer.skeletons.domain.model.Safebox;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

@ExtendWith(MockitoExtension.class)
class InputSafeboxMapperUnitTest {

    @InjectMocks
    private InputSafeboxMapper inputSafeboxMapper;

    @Mock
    private ModelMapper modelMapper;

    @Test
    void mapUnitTest() {
        Safebox safebox = new Safebox();

        inputSafeboxMapper.map(safebox);

        Mockito.verify(modelMapper).map(safebox, SafeboxDto.class);
    }
}
