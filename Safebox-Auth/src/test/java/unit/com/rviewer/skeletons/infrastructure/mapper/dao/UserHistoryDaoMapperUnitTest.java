package unit.com.rviewer.skeletons.infrastructure.mapper.dao;

import com.rviewer.skeletons.domain.model.user.SafeboxUserHistory;
import com.rviewer.skeletons.infrastructure.mapper.dao.UserHistoryDaoMapper;
import com.rviewer.skeletons.infrastructure.persistence.dao.UserHistoryDao;
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
class UserHistoryDaoMapperUnitTest {

    @InjectMocks
    private UserHistoryDaoMapper userHistoryDaoMapper;

    @Mock
    private ModelMapper modelMapper;

    @Test
    void mapUnitTest() {
        UserHistoryDao userDao = new UserHistoryDao();

        userHistoryDaoMapper.map(userDao);

        Mockito.verify(modelMapper).map(userDao, SafeboxUserHistory.class);
    }

    @Test
    void mapListUnitTest() {
        List<UserHistoryDao> userHistoryDaoList = Collections.singletonList(new UserHistoryDao());

        userHistoryDaoMapper.map(userHistoryDaoList);

        userHistoryDaoList.forEach(userHistoryDao -> Mockito.verify(modelMapper).map(userHistoryDao, SafeboxUserHistory.class));
    }
}
