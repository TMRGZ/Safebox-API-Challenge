package integration.com.rviewer.skeletons.infrastructure.controller.impl;

import com.rviewer.skeletons.application.model.UserDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MvcResult;

import java.net.URI;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TokenApiImplIntegrationTest extends AbstractControllerIntegrationTest {

    private static final String DECODE_TOKEN_URL = "/token/decode";

    @Test
    void decodeTokenIntegrationTest() throws Exception {
        URI uri = URI.create(DECODE_TOKEN_URL);
        SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor jwt = jwt();

        MvcResult result = mockMvc.perform(get(uri).with(jwt)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn();

        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getResponse());

        UserDto response = objectMapper.readValue(result.getResponse().getContentAsString(), UserDto.class);

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getUsername());
    }
}
