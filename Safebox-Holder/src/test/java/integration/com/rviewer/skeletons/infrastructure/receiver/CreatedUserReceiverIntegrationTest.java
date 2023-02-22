package integration.com.rviewer.skeletons.infrastructure.receiver;

import com.rviewer.skeletons.SafeboxHolderApplication;
import com.rviewer.skeletons.infrastructure.persistence.dao.SafeboxDao;
import com.rviewer.skeletons.infrastructure.persistence.repository.JpaSafeboxRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import java.util.Optional;

@SpringBootTest(classes = SafeboxHolderApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CreatedUserReceiverIntegrationTest {

    @Autowired
    private InputDestination inputDestination;

    @Autowired
    private JpaSafeboxRepository safeboxRepository;

    @Test
    void receiveIntegrationTest() {
        String owner = "CREATE_USER_BY_RABBIT";
        Message<String> ownerMessage = MessageBuilder.withPayload(owner).build();
        Optional<SafeboxDao> byOwner = safeboxRepository.findByOwner(owner);
        Assertions.assertTrue(byOwner.isEmpty());

        inputDestination.send(ownerMessage);

        byOwner = safeboxRepository.findByOwner(owner);
        Assertions.assertTrue(byOwner.isPresent());
        Assertions.assertNotNull(byOwner.get().getItemList());
        Assertions.assertTrue(byOwner.get().getItemList().isEmpty());
    }

    @Test
    void receive_alreadyExistingSafeboxException_IntegrationTest() {
        String owner = "EXISTING_SAFEBOX";
        Message<String> ownerMessage = MessageBuilder.withPayload(owner).build();
        Optional<SafeboxDao> byOwner = safeboxRepository.findByOwner(owner);
        Assertions.assertTrue(byOwner.isPresent());
        long countBefore = safeboxRepository.count();

        inputDestination.send(ownerMessage);

        long countAfter = safeboxRepository.count();

        Assertions.assertEquals(countBefore, countAfter);
    }
}
