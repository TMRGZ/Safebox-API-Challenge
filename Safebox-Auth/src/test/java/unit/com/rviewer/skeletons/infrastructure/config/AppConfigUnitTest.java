package unit.com.rviewer.skeletons.infrastructure.config;

import com.rviewer.skeletons.infrastructure.config.AppConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class AppConfigUnitTest {

    @Test
    void creationUnitTest() {
        AppConfig appConfig = new AppConfig();
        appConfig.setMaxTries(0);
        appConfig.setTokenSecret("TEST");
        appConfig.setTokenExpirationMinutes(0);

        Assertions.assertNotNull(appConfig);
        Assertions.assertNotNull(appConfig.getMaxTries());
        Assertions.assertNotNull(appConfig.getTokenSecret());
        Assertions.assertNotNull(appConfig.getTokenExpirationMinutes());
    }
}
