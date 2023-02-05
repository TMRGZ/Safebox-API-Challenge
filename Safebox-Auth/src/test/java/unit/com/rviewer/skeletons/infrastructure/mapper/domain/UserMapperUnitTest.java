package unit.com.rviewer.skeletons.infrastructure.mapper.domain;

import com.rviewer.skeletons.domain.model.user.SafeboxUser;
import com.rviewer.skeletons.infrastructure.mapper.domain.UserMapper;
import com.rviewer.skeletons.infrastructure.persistence.dao.UserDao;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;

import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class UserMapperUnitTest {

    @InjectMocks
    private UserMapper userMapper;

    @Mock
    private ModelMapper modelMapper;

    @Test
    void setupMapperUnitTest() {
        TypeMap<SafeboxUser, UserDao> propertyMapper = Mockito.mock(TypeMap.class);
        Mockito.when(modelMapper.createTypeMap(SafeboxUser.class, UserDao.class)).thenReturn(propertyMapper);

        userMapper.setup();

        Mockito.verify(modelMapper).createTypeMap(SafeboxUser.class, UserDao.class);
    }

    @Test
    void mapUnitTest() {
        SafeboxUser safeboxUser = new SafeboxUser();

        userMapper.map(safeboxUser);

        Mockito.verify(modelMapper).map(safeboxUser, UserDao.class);
    }

    @Test
    void mapListUnitTest() {
        List<SafeboxUser> safeboxUserList = Collections.singletonList(new SafeboxUser());

        userMapper.map(safeboxUserList);

        safeboxUserList.forEach(safeboxUser -> Mockito.verify(modelMapper).map(safeboxUser, UserDao.class));
    }
}
