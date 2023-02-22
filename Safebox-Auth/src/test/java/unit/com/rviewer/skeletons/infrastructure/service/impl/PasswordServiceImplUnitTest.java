package unit.com.rviewer.skeletons.infrastructure.service.impl;

import com.rviewer.skeletons.infrastructure.service.impl.PasswordServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class PasswordServiceImplUnitTest {

    @InjectMocks
    private PasswordServiceImpl passwordService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void encodePasswordUnitTest() {
        String password = "TEST";

        passwordService.encodePassword(password);

        Mockito.verify(passwordEncoder).encode(password);
    }

    @Test
    void checkPasswordUnitTest() {
        String password = "TEST";
        String encodedPassword = "ENCODED_TEST";

        passwordService.checkPassword(encodedPassword, password);

        Mockito.verify(passwordEncoder).matches(password, encodedPassword);
    }
}
