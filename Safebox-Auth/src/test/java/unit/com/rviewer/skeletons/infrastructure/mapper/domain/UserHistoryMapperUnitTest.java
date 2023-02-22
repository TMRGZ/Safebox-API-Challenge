package unit.com.rviewer.skeletons.infrastructure.mapper.domain;

import com.rviewer.skeletons.domain.model.user.SafeboxUserHistory;
import com.rviewer.skeletons.infrastructure.mapper.domain.UserHistoryMapper;
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
class UserHistoryMapperUnitTest {

    @InjectMocks
    private UserHistoryMapper userHistoryMapper;

    @Mock
    private ModelMapper modelMapper;

    @Test
    void mapUnitTest() {
        SafeboxUserHistory safeboxUserHistory = new SafeboxUserHistory();

        userHistoryMapper.map(safeboxUserHistory);

        Mockito.verify(modelMapper).map(safeboxUserHistory, UserHistoryDao.class);
    }

    @Test
    void mapListUnitTest() {
        List<SafeboxUserHistory> safeboxUserHistoryList = Collections.singletonList(new SafeboxUserHistory());

        userHistoryMapper.map(safeboxUserHistoryList);

        safeboxUserHistoryList.forEach(safeboxUserHistory -> Mockito.verify(modelMapper).map(safeboxUserHistory, UserHistoryDao.class));
    }
}
