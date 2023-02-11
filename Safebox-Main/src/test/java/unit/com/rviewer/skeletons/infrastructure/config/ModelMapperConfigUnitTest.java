package unit.com.rviewer.skeletons.infrastructure.config;

import com.rviewer.skeletons.infrastructure.config.ModelMapperConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

@ExtendWith(MockitoExtension.class)
class ModelMapperConfigUnitTest {

    @InjectMocks
    private ModelMapperConfig modelMapperConfig;

    @Test
    void modelMapperUnitTest() {
        ModelMapper modelMapper = modelMapperConfig.modelMapper();

        Assertions.assertNotNull(modelMapper);
    }

}
