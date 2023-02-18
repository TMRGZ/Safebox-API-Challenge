package integration.com.rviewer.skeletons.infrastructure.controller.impl;

import com.rviewer.skeletons.application.model.LoginResponseDto;
import com.rviewer.skeletons.domain.model.event.EventResultEnum;
import com.rviewer.skeletons.domain.model.event.EventTypeEnum;
import com.rviewer.skeletons.infrastructure.persistence.dao.UserDao;
import com.rviewer.skeletons.infrastructure.persistence.dao.UserHistoryDao;
import com.rviewer.skeletons.infrastructure.persistence.repository.JpaUserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Comparator;
import java.util.Optional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class LoginApiImplIntegrationTest extends AbstractControllerIntegrationTest {

    private static final String LOGIN_URL = "/login";

    @Autowired
    private JpaUserRepository userRepository;

    @Test
    void loginUser_IntegrationTest() throws Exception {
        String username = "REGISTERED_USER";
        String password = "REGISTERED_USER";
        String id = "REGISTERED_USER";

        URI uri = UriComponentsBuilder.fromUriString(LOGIN_URL).build(id);
        MvcResult result = mockMvc.perform(post(uri).with(httpBasic(username, password))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated()).andReturn();

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
    void loginUser_withoutAuthentication_IntegrationTest() throws Exception {
        URI uri = URI.create(LOGIN_URL);
        mockMvc.perform(post(uri)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isUnauthorized());
    }

    @Test
    void loginUser_userIsLocked_IntegrationTest() throws Exception {
        String username = "LOCKED_USER";
        String password = "LOCKED_USER";
        String id = "LOCKED_USER";

        URI uri = UriComponentsBuilder.fromUriString(LOGIN_URL).build(id);
        mockMvc.perform(post(uri).with(httpBasic(username, password))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isLocked());

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
    void loginUser_userIdDoesNotExist_IntegrationTest() throws Exception {
        String username = "NOT_REGISTERED_USER";
        String password = "NOT_REGISTERED_USER";

        URI uri = URI.create(LOGIN_URL);
        mockMvc.perform(post(uri).with(httpBasic(username, password))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isForbidden());
    }

    @Test
    void loginUser_badPassword_IntegrationTest() throws Exception {
        String username = "BAD_PASSWORD";
        String password = "BAD_PASSWORD";
        String id = "BAD_PASSWORD";

        URI uri = UriComponentsBuilder.fromUriString(LOGIN_URL).build(id);
        mockMvc.perform(post(uri).with(httpBasic(username, password))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isForbidden());

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
        Assertions.assertFalse(lastHistory.getLocked());
        Assertions.assertNotNull(lastHistory.getCurrentTries());
        Assertions.assertEquals(1, lastHistory.getCurrentTries());
    }
}
