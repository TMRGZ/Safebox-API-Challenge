package unit.com.rviewer.skeletons.application.mapper;

import com.rviewer.skeletons.application.mapper.InputUserMapper;
import com.rviewer.skeletons.application.model.CreateSafeboxRequestDto;
import com.rviewer.skeletons.domain.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

@ExtendWith(MockitoExtension.class)
class InputUserMapperUnitTest {

    @InjectMocks
    private InputUserMapper userMapper;

    @Mock
    private ModelMapper modelMapper;

    @Test
    void mapUnitTest() {
        CreateSafeboxRequestDto createSafeboxRequestDto = new CreateSafeboxRequestDto();

        userMapper.map(createSafeboxRequestDto);

        Mockito.verify(modelMapper).map(createSafeboxRequestDto, User.class);
    }
}
