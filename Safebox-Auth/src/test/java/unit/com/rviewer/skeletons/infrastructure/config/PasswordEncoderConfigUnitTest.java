package unit.com.rviewer.skeletons.infrastructure.config;

import com.rviewer.skeletons.infrastructure.config.PasswordEncoderConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PasswordEncoderConfigUnitTest {

    @InjectMocks
    private PasswordEncoderConfig passwordEncoderConfig;

    @Test
    void passwordEncoderUnitTest() {
        Assertions.assertNotNull(passwordEncoderConfig.passwordEncoder());
    }
}
