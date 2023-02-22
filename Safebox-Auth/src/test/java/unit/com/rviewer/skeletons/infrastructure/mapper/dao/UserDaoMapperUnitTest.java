package unit.com.rviewer.skeletons.infrastructure.mapper.dao;

import com.rviewer.skeletons.domain.model.user.SafeboxUser;
import com.rviewer.skeletons.infrastructure.mapper.dao.UserDaoMapper;
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
class UserDaoMapperUnitTest {

    @InjectMocks
    private UserDaoMapper userDaoMapper;

    @Mock
    private ModelMapper modelMapper;

    @Test
    void setupMapperUnitTest() {
        TypeMap<UserDao, SafeboxUser> propertyMapper = Mockito.mock(TypeMap.class);
        Mockito.when(modelMapper.createTypeMap(UserDao.class, SafeboxUser.class)).thenReturn(propertyMapper);

        userDaoMapper.setup();

        Mockito.verify(modelMapper).createTypeMap(UserDao.class, SafeboxUser.class);
    }

    @Test
    void mapUnitTest() {
        UserDao userDao = new UserDao();

        userDaoMapper.map(userDao);

        Mockito.verify(modelMapper).map(userDao, SafeboxUser.class);
    }

    @Test
    void mapListUnitTest() {
        List<UserDao> userDaoList = Collections.singletonList(new UserDao());

        userDaoMapper.map(userDaoList);

        userDaoList.forEach(userDao -> Mockito.verify(modelMapper).map(userDao, SafeboxUser.class));
    }
}
