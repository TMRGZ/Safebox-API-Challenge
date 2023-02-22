package unit.com.rviewer.skeletons.infrastructure.config;

import com.rviewer.skeletons.infrastructure.config.ModelMapperConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ModeMapperConfigUnitTest {

    @InjectMocks
    private ModelMapperConfig modelMapperConfig;

    @Test
    void modelMapperUnitTest() {
        Assertions.assertNotNull(modelMapperConfig.modelMapper());
    }
}
