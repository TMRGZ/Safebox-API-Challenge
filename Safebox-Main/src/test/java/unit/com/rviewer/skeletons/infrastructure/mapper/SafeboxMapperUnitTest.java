package unit.com.rviewer.skeletons.infrastructure.mapper;

import com.rviewer.skeletons.domain.model.Safebox;
import com.rviewer.skeletons.infrastructure.mapper.SafeboxMapper;
import com.rviewer.skeletons.infrastructure.rest.safebox.holder.model.HolderSafeboxDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

@ExtendWith(MockitoExtension.class)
class SafeboxMapperUnitTest {

    @InjectMocks
    private SafeboxMapper safeboxMapper;

    @Mock
    private ModelMapper modelMapper;

    @Test
    void mapUnitTest() {
        HolderSafeboxDto safeboxDto = new HolderSafeboxDto();

        safeboxMapper.map(safeboxDto);

        Mockito.verify(modelMapper).map(safeboxDto, Safebox.class);
    }
}
