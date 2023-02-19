package integration.com.rviewer.skeletons.infrastructure.controller.impl;

import com.rviewer.skeletons.application.model.CreateUserDto;
import com.rviewer.skeletons.application.model.RegisteredUserDto;
import com.rviewer.skeletons.domain.model.event.EventResultEnum;
import com.rviewer.skeletons.domain.model.event.EventTypeEnum;
import com.rviewer.skeletons.infrastructure.persistence.dao.UserDao;
import com.rviewer.skeletons.infrastructure.persistence.dao.UserHistoryDao;
import com.rviewer.skeletons.infrastructure.persistence.repository.JpaUserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.http.MediaType;
import org.springframework.messaging.Message;
import org.springframework.test.web.servlet.MvcResult;

import java.net.URI;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserApiImplIntegrationTest extends AbstractControllerIntegrationTest {

    private static final String REGISTER_URL = "/user";

    @Autowired
    private JpaUserRepository userRepository;

    @Autowired
    private OutputDestination outputDestination;

    @BeforeEach
    void setup() {
        outputDestination.clear();
    }

    @AfterEach
    void cleanup() {
        outputDestination.clear();
    }

    @Test
    void postUserIntegrationTest() throws Exception {
        String username = "TEST_USER";
        String password = "TEST_USER";
        CreateUserDto newUserRequest = new CreateUserDto().username(username).password(password);
        URI uri = new URI(REGISTER_URL);

        MvcResult result = mockMvc.perform(post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newUserRequest))
        ).andExpect(status().isCreated()).andReturn();

        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getResponse());

        RegisteredUserDto response = objectMapper.readValue(result.getResponse().getContentAsString(), RegisteredUserDto.class);

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getId());

        Optional<UserDao> savedUserOptional = userRepository.findById(response.getId());

        Assertions.assertTrue(savedUserOptional.isPresent());

        UserDao savedUser = savedUserOptional.get();

        Assertions.assertNotNull(savedUser.getId());
        Assertions.assertNotNull(savedUser.getUsername());
        Assertions.assertEquals(username, savedUser.getUsername());
        Assertions.assertNotNull(savedUser.getPassword());
        Assertions.assertNotEquals(password, savedUser.getPassword());
        Assertions.assertNotNull(savedUser.getUserHistory());
        Assertions.assertFalse(savedUser.getUserHistory().isEmpty());
        Assertions.assertEquals(1, savedUser.getUserHistory().size());

        UserHistoryDao history = savedUser.getUserHistory().get(0);
        Assertions.assertNotNull(history);
        Assertions.assertNotNull(history.getEventDate());
        Assertions.assertNotNull(history.getEventTypeEnum());
        Assertions.assertEquals(EventTypeEnum.CREATION, history.getEventTypeEnum());
        Assertions.assertNotNull(history.getEventResultEnum());
        Assertions.assertEquals(EventResultEnum.SUCCESSFUL, history.getEventResultEnum());
        Assertions.assertNotNull(history.getLocked());
        Assertions.assertFalse(history.getLocked());
        Assertions.assertNotNull(history.getCurrentTries());
        Assertions.assertEquals(0, history.getCurrentTries());

        String sentId = messageToString(outputDestination.receive());

        Assertions.assertNotNull(sentId);
        Assertions.assertEquals(savedUser.getId(), sentId);
    }

    @Test
    void safeboxAuthUserPost_alreadyRegisteredUser409_IntegrationTest() throws Exception {
        String username = "REGISTERED_USER";
        String password = "REGISTERED_USER";
        CreateUserDto newUserRequest = new CreateUserDto().username(username).password(password);
        URI uri = new URI(REGISTER_URL);

        mockMvc.perform(post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newUserRequest))
        ).andExpect(status().isConflict());

        String sentId = messageToString(outputDestination.receive());

        Assertions.assertNull(sentId);
    }

    private String messageToString(Message<byte[]> message) {
        return Optional.ofNullable(message).map(m -> new String(message.getPayload())).orElse(null);
    }
}
