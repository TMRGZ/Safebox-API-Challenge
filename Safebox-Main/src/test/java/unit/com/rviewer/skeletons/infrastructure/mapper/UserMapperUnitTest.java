package unit.com.rviewer.skeletons.infrastructure.mapper;

import com.rviewer.skeletons.domain.model.User;
import com.rviewer.skeletons.infrastructure.mapper.UserMapper;
import com.rviewer.skeletons.infrastructure.rest.safebox.auth.model.AuthLoginResponseDto;
import com.rviewer.skeletons.infrastructure.rest.safebox.auth.model.AuthRegisteredUserDto;
import com.rviewer.skeletons.infrastructure.rest.safebox.auth.model.AuthUserDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

@ExtendWith(MockitoExtension.class)
class UserMapperUnitTest {

    @InjectMocks
    private UserMapper userMapper;

    @Mock
    private ModelMapper modelMapper;

    @Test
    void mapUserDtoUnitTest() {
        AuthUserDto userDto = new AuthUserDto();

        userMapper.map(userDto);

        Mockito.verify(modelMapper).map(userDto, User.class);
    }

    @Test
    void mapRegisteredUserDtoUnitTest() {
        AuthRegisteredUserDto userDto = new AuthRegisteredUserDto();

        userMapper.map(userDto);

        Mockito.verify(modelMapper).map(userDto, User.class);
    }

    @Test
    void mapLoginDtoUnitTest() {
        AuthLoginResponseDto loginDto = new AuthLoginResponseDto();

        userMapper.map(loginDto);

        Mockito.verify(modelMapper).map(loginDto, User.class);
    }
}
