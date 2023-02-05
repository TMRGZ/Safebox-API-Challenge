package unit.com.rviewer.skeletons.infrastructure.persitence.repository.impl;

import com.rviewer.skeletons.domain.model.user.SafeboxUser;
import com.rviewer.skeletons.infrastructure.mapper.dao.UserDaoMapper;
import com.rviewer.skeletons.infrastructure.mapper.domain.UserMapper;
import com.rviewer.skeletons.infrastructure.persistence.dao.UserDao;
import com.rviewer.skeletons.infrastructure.persistence.repository.JpaUserRepository;
import com.rviewer.skeletons.infrastructure.persistence.repository.impl.SafeboxUserRepositoryImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class SafeboxUserRepositoryImplUnitTest {

    @InjectMocks
    private SafeboxUserRepositoryImpl safeboxUserRepository;

    @Mock
    private JpaUserRepository jpaUserRepository;

    @Mock
    private UserDaoMapper userDaoMapper;

    @Mock
    private UserMapper userMapper;

    @Test
    void findByIdUnitTest() {
        String id = "TEST";
        Mockito.when(jpaUserRepository.findById(id)).thenReturn(Optional.of(new UserDao()));

        safeboxUserRepository.findById(id);

        Mockito.verify(jpaUserRepository).findById(id);
        Mockito.verify(userDaoMapper).map(Mockito.any(UserDao.class));
    }

    @Test
    void findByUsernameUnitTest() {
        String username = "TEST";
        Mockito.when(jpaUserRepository.findByUsername(username)).thenReturn(Optional.of(new UserDao()));

        safeboxUserRepository.findByUsername(username);

        Mockito.verify(jpaUserRepository).findByUsername(username);
        Mockito.verify(userDaoMapper).map(Mockito.any(UserDao.class));
    }

    @Test
    void saveUnitTest() {
        SafeboxUser user = new SafeboxUser();
        UserDao userDao = new UserDao();
        Mockito.when(userMapper.map(user)).thenReturn(userDao);
        Mockito.when(jpaUserRepository.save(userDao)).thenReturn(userDao);

        safeboxUserRepository.save(user);

        Mockito.verify(userMapper).map(user);
        Mockito.verify(jpaUserRepository).save(userDao);
        Mockito.verify(userDaoMapper).map(userDao);

    }
}
