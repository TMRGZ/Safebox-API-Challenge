package unit.com.rviewer.skeletons.infrastructure.persistence.repository.impl;

import com.rviewer.skeletons.domain.model.Safebox;
import com.rviewer.skeletons.infrastructure.mapper.dao.SafeboxDaoMapper;
import com.rviewer.skeletons.infrastructure.mapper.domain.SafeboxMapper;
import com.rviewer.skeletons.infrastructure.persistence.dao.SafeboxDao;
import com.rviewer.skeletons.infrastructure.persistence.repository.JpaSafeboxRepository;
import com.rviewer.skeletons.infrastructure.persistence.repository.impl.SafeboxRepositoryImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class SafeboxRepositoryImplUnitTest {

    @InjectMocks
    private SafeboxRepositoryImpl safeboxRepository;

    @Mock
    private JpaSafeboxRepository jpaSafeboxRepository;

    @Mock
    private SafeboxMapper safeboxMapper;

    @Mock
    private SafeboxDaoMapper safeboxDaoMapper;

    @Test
    void findByIdUnitTest() {
        Mockito.when(jpaSafeboxRepository.findById(Mockito.any())).thenReturn(Optional.of(new SafeboxDao()));

        safeboxRepository.findById(UUID.randomUUID().toString());

        Mockito.verify(jpaSafeboxRepository).findById(Mockito.any());
        Mockito.verify(safeboxDaoMapper).map(Mockito.any(SafeboxDao.class));
    }

    @Test
    void findByOwnerUnitTest() {
        Mockito.when(jpaSafeboxRepository.findByOwner(Mockito.any())).thenReturn(Optional.of(new SafeboxDao()));

        safeboxRepository.findByOwner("TEST");

        Mockito.verify(jpaSafeboxRepository).findByOwner(Mockito.any());
        Mockito.verify(safeboxDaoMapper).map(Mockito.any(SafeboxDao.class));
    }

    @Test
    void saveUnitTest() {
        Mockito.when(safeboxMapper.map(Mockito.any(Safebox.class))).thenReturn(new SafeboxDao());
        Mockito.when(jpaSafeboxRepository.save(Mockito.any())).thenReturn(new SafeboxDao());

        safeboxRepository.save(new Safebox());

        Mockito.verify(jpaSafeboxRepository).save(Mockito.any());
        Mockito.verify(safeboxMapper).map(Mockito.any(Safebox.class));
        Mockito.verify(safeboxDaoMapper).map(Mockito.any(SafeboxDao.class));
    }
}
