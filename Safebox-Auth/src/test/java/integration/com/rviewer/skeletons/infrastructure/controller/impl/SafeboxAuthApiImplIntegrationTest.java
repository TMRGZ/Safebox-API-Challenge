package integration.com.rviewer.skeletons.infrastructure.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rviewer.skeletons.SafeboxAuthApplication;
import com.rviewer.skeletons.application.model.LoginResponseDto;
import com.rviewer.skeletons.application.model.RegisteredUserDto;
import com.rviewer.skeletons.application.model.UserDto;
import com.rviewer.skeletons.domain.model.event.EventResultEnum;
import com.rviewer.skeletons.domain.model.event.EventTypeEnum;
import com.rviewer.skeletons.infrastructure.persistence.dao.UserDao;
import com.rviewer.skeletons.infrastructure.persistence.dao.UserHistoryDao;
import com.rviewer.skeletons.infrastructure.persistence.repository.JpaUserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Comparator;
import java.util.Optional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = SafeboxAuthApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class SafeboxAuthApiImplIntegrationTest {

    private static final String LOGIN_URL = "/safebox-auth/{id}/login";
    private static final String REGISTER_URL = "/safebox-auth/user";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private JpaUserRepository userRepository;

    @Test
    void safeboxAuthUserPostIntegrationTest() throws Exception {
        String username = "TEST_USER";
        String password = "TEST_USER";
        UserDto newUserRequest = new UserDto().username(username).password(password);
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
    }

    @Test
    void safeboxAuthUserPost_alreadyRegisteredUser409_IntegrationTest() throws Exception {
        String username = "REGISTERED_USER";
        String password = "REGISTERED_USER";
        UserDto newUserRequest = new UserDto().username(username).password(password);
        URI uri = new URI(REGISTER_URL);

        mockMvc.perform(post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newUserRequest))
        ).andExpect(status().isConflict());
    }

    @Test
    void safeboxAuthIdLoginPost_IntegrationTest() throws Exception {
        String username = "REGISTERED_USER";
        String password = "REGISTERED_USER";
        String id = "REGISTERED_USER";

        URI uri = UriComponentsBuilder.fromUriString(LOGIN_URL).build(id);
        MvcResult result = mockMvc.perform(post(uri).with(httpBasic(username, password))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn();

        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getResponse());

        LoginResponseDto response = objectMapper.readValue(result.getResponse().getContentAsString(), LoginResponseDto.class);

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getToken());

        Optional<UserDao> byIdOptional = userRepository.findById(id);

        Assertions.assertTrue(byIdOptional.isPresent());

        UserDao byId = byIdOptional.get();

        Assertions.assertNotNull(byId.getUsername());
        Assertions.assertEquals(username, byId.getUsername());
        Assertions.assertNotNull(byId.getPassword());
        Assertions.assertNotEquals(password, byId.getPassword());
        Assertions.assertNotNull(byId.getUserHistory());

        byId.getUserHistory().stream()
                .reduce((h1, h2) -> {
                    Assertions.assertEquals(-1, h1.getEventDate().compareTo(h2.getEventDate()));
                    return h2;
                });

        UserHistoryDao lastHistory = byId.getUserHistory().stream()
                .max(Comparator.comparing(UserHistoryDao::getEventDate))
                .orElse(null);

        Assertions.assertNotNull(lastHistory);
        Assertions.assertNotNull(lastHistory.getEventDate());
        Assertions.assertNotNull(lastHistory.getEventTypeEnum());
        Assertions.assertEquals(EventTypeEnum.LOGIN, lastHistory.getEventTypeEnum());
        Assertions.assertNotNull(lastHistory.getEventResultEnum());
        Assertions.assertEquals(EventResultEnum.SUCCESSFUL, lastHistory.getEventResultEnum());
        Assertions.assertNotNull(lastHistory.getLocked());
        Assertions.assertFalse(lastHistory.getLocked());
        Assertions.assertNotNull(lastHistory.getCurrentTries());
        Assertions.assertEquals(0, lastHistory.getCurrentTries());
    }

    @Test
    void safeboxAuthIdLoginPost_userDoesLocked_IntegrationTest() throws Exception {
        String username = "LOCKED_USER";
        String password = "LOCKED_USER";
        String id = "LOCKED_USER";

        URI uri = UriComponentsBuilder.fromUriString(LOGIN_URL).build(id);
        mockMvc.perform(post(uri).with(httpBasic(username, password))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isUnauthorized());

        Optional<UserDao> byIdOptional = userRepository.findById(id);

        Assertions.assertTrue(byIdOptional.isPresent());

        UserDao byId = byIdOptional.get();

        Assertions.assertNotNull(byId.getUsername());
        Assertions.assertEquals(username, byId.getUsername());
        Assertions.assertNotNull(byId.getPassword());
        Assertions.assertNotEquals(password, byId.getPassword());
        Assertions.assertNotNull(byId.getUserHistory());

        byId.getUserHistory().stream()
                .reduce((h1, h2) -> {
                    Assertions.assertEquals(-1, h1.getEventDate().compareTo(h2.getEventDate()));
                    return h2;
                });

        UserHistoryDao lastHistory = byId.getUserHistory().stream()
                .max(Comparator.comparing(UserHistoryDao::getEventDate))
                .orElse(null);

        Assertions.assertNotNull(lastHistory);
        Assertions.assertNotNull(lastHistory.getEventDate());
        Assertions.assertNotNull(lastHistory.getEventTypeEnum());
        Assertions.assertEquals(EventTypeEnum.LOGIN, lastHistory.getEventTypeEnum());
        Assertions.assertNotNull(lastHistory.getEventResultEnum());
        Assertions.assertEquals(EventResultEnum.FAILED, lastHistory.getEventResultEnum());
        Assertions.assertNotNull(lastHistory.getLocked());
        Assertions.assertTrue(lastHistory.getLocked());
        Assertions.assertNotNull(lastHistory.getCurrentTries());
        Assertions.assertEquals(4, lastHistory.getCurrentTries());
    }

    @Test
    void safeboxAuthIdLoginPost_userIdDoesNotExist_IntegrationTest() throws Exception {
        String username = "REGISTERED_USER";
        String password = "REGISTERED_USER";
        String id = "ID_DOES_NOT_EXIST";

        URI uri = UriComponentsBuilder.fromUriString(LOGIN_URL).build(id);
        mockMvc.perform(post(uri).with(httpBasic(username, password))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound());

        Optional<UserDao> byIdOptional = userRepository.findByUsername(username);

        Assertions.assertTrue(byIdOptional.isPresent());

        UserDao byId = byIdOptional.get();

        Assertions.assertNotNull(byId.getUsername());
        Assertions.assertEquals(username, byId.getUsername());
        Assertions.assertNotNull(byId.getPassword());
        Assertions.assertNotEquals(password, byId.getPassword());
        Assertions.assertNotNull(byId.getUserHistory());

        byId.getUserHistory().stream()
                .reduce((h1, h2) -> {
                    Assertions.assertEquals(-1, h1.getEventDate().compareTo(h2.getEventDate()));
                    return h2;
                });

        UserHistoryDao lastHistory = byId.getUserHistory().stream()
                .max(Comparator.comparing(UserHistoryDao::getEventDate))
                .orElse(null);

        Assertions.assertNotNull(lastHistory);
        Assertions.assertNotNull(lastHistory.getEventDate());
        Assertions.assertNotNull(lastHistory.getEventTypeEnum());
        Assertions.assertEquals(EventTypeEnum.LOGIN, lastHistory.getEventTypeEnum());
        Assertions.assertNotNull(lastHistory.getEventResultEnum());
        Assertions.assertEquals(EventResultEnum.SUCCESSFUL, lastHistory.getEventResultEnum());
        Assertions.assertNotNull(lastHistory.getLocked());
        Assertions.assertFalse(lastHistory.getLocked());
        Assertions.assertNotNull(lastHistory.getCurrentTries());
        Assertions.assertEquals(0, lastHistory.getCurrentTries());
    }

}
