package integration.com.rviewer.skeletons.infrastructure.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.rviewer.skeletons.SafeboxMainApplication;
import com.rviewer.skeletons.application.model.CreateSafeboxRequestDto;
import com.rviewer.skeletons.application.model.CreatedSafeboxDto;
import com.rviewer.skeletons.application.model.ItemListDto;
import com.rviewer.skeletons.application.model.SafeboxKeyDto;
import com.rviewer.skeletons.infrastructure.rest.safebox.auth.model.AuthLoginResponseDto;
import com.rviewer.skeletons.infrastructure.rest.safebox.auth.model.AuthRegisteredUserDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Collections;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@AutoConfigureWireMock(port = 0)
@SpringBootTest(classes = SafeboxMainApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SafeboxApiImplIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String SAFEBOX_BASE_URL = "/safebox";

    private static final String OPEN_SAFEBOX_URL = SAFEBOX_BASE_URL + "/{id}/open";

    private static final String SAFEBOX_ITEMS_URL = SAFEBOX_BASE_URL + "/{id}/items";

    private static final String SAFEBOX_AUTH_POST_USER_URL = "/safebox-auth/user";

    private static final String SAFEBOX_AUTH_LOGIN_URL = "/safebox-auth/login";

    @Test
    void postSafeboxIntegrationTest() throws Exception {
        CreateSafeboxRequestDto createSafeboxRequestDto = new CreateSafeboxRequestDto();
        createSafeboxRequestDto.setName("TEST-USER");
        createSafeboxRequestDto.setPassword("TEST-PASSWORD");

        AuthRegisteredUserDto registeredUserDto = new AuthRegisteredUserDto();
        registeredUserDto.setId("TEST-ID");

        URI safeboxAuthUserUri = URI.create(SAFEBOX_AUTH_POST_USER_URL);
        WireMock.stubFor(WireMock.post(WireMock.urlEqualTo(safeboxAuthUserUri.getPath()))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(objectMapper.writeValueAsString(registeredUserDto))
                )
        );

        URI baseSafeboxUri = URI.create(SAFEBOX_BASE_URL);
        MvcResult result = mockMvc.perform(post(baseSafeboxUri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createSafeboxRequestDto))
        ).andExpect(status().isCreated()).andReturn();

        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getResponse());

        CreatedSafeboxDto response = objectMapper.readValue(result.getResponse().getContentAsString(), CreatedSafeboxDto.class);

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getId());
    }

    @Test
    void postSafebox_alreadyExistingSafebox409_IntegrationTest() throws Exception {
        CreateSafeboxRequestDto createSafeboxRequestDto = new CreateSafeboxRequestDto();
        createSafeboxRequestDto.setName("TEST-USER");
        createSafeboxRequestDto.setPassword("TEST-PASSWORD");

        URI safeboxAuthUserUri = URI.create(SAFEBOX_AUTH_POST_USER_URL);
        WireMock.stubFor(WireMock.post(WireMock.urlEqualTo(safeboxAuthUserUri.getPath()))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withStatus(HttpStatus.CONFLICT.value())
                )
        );

        URI baseSafeboxUri = URI.create(SAFEBOX_BASE_URL);
        mockMvc.perform(post(baseSafeboxUri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createSafeboxRequestDto))
        ).andExpect(status().isConflict());
    }

    @Test
    void postSafebox_authServerFailed_IntegrationTest() throws Exception {
        CreateSafeboxRequestDto createSafeboxRequestDto = new CreateSafeboxRequestDto();
        createSafeboxRequestDto.setName("TEST-USER");
        createSafeboxRequestDto.setPassword("TEST-PASSWORD");

        URI safeboxAuthUserUri = URI.create(SAFEBOX_AUTH_POST_USER_URL);
        WireMock.stubFor(WireMock.post(WireMock.urlEqualTo(safeboxAuthUserUri.getPath()))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                )
        );

        URI baseSafeboxUri = URI.create(SAFEBOX_BASE_URL);
        mockMvc.perform(post(baseSafeboxUri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createSafeboxRequestDto))
        ).andExpect(status().isBadGateway());
    }

    @Test
    void openSafeboxIntegrationTest() throws Exception {
        String id = "TEST-ID";
        String username = "TEST-USER";
        String password = "TEST-PASSWORD";
        String token = "TOKEN";

        AuthLoginResponseDto loginResponseDto = new AuthLoginResponseDto();
        loginResponseDto.setToken(token);

        URI safeboxAuthLoginUri = URI.create(SAFEBOX_AUTH_LOGIN_URL);
        WireMock.stubFor(WireMock.post(WireMock.urlEqualTo(safeboxAuthLoginUri.getPath()))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(objectMapper.writeValueAsString(loginResponseDto))
                )
        );

        URI openSafeboxUri = UriComponentsBuilder.fromUriString(OPEN_SAFEBOX_URL).build(id);
        MvcResult result = mockMvc.perform(get(openSafeboxUri).with(httpBasic(username, password))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn();

        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getResponse());

        SafeboxKeyDto response = objectMapper.readValue(result.getResponse().getContentAsString(), SafeboxKeyDto.class);

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getToken());
        Assertions.assertEquals(token, response.getToken());
    }

    @Test
    void openSafebox_noAuthentication401_IntegrationTest() throws Exception {
        String id = "TEST-ID";

        URI openSafeboxUri = UriComponentsBuilder.fromUriString(OPEN_SAFEBOX_URL).build(id);
        mockMvc.perform(get(openSafeboxUri)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isUnauthorized());
    }

    @Test
    void openSafebox_safeboxLocked423_IntegrationTest() throws Exception {
        String id = "TEST-ID";
        String username = "TEST-USER";
        String password = "TEST-PASSWORD";

        URI safeboxAuthLoginUri = URI.create(SAFEBOX_AUTH_LOGIN_URL);
        WireMock.stubFor(WireMock.post(WireMock.urlEqualTo(safeboxAuthLoginUri.getPath()))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withStatus(HttpStatus.LOCKED.value())
                )
        );

        URI openSafeboxUri = UriComponentsBuilder.fromUriString(OPEN_SAFEBOX_URL).build(id);
        mockMvc.perform(get(openSafeboxUri).with(httpBasic(username, password))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isLocked());
    }

    @Test
    void getSafeboxItemsIntegrationTest() throws Exception {
        String id = "TEST-ID";

        URI baseSafeboxUri = UriComponentsBuilder.fromUriString(SAFEBOX_ITEMS_URL).build(id);
        MvcResult result = mockMvc.perform(get(baseSafeboxUri).with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn();

        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getResponse());

        ItemListDto response = objectMapper.readValue(result.getResponse().getContentAsString(), ItemListDto.class);

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getItems());
    }

    @Test
    void getSafeboxItems_noAuthentication401_IntegrationTest() throws Exception {
        String id = "TEST-ID";

        URI baseSafeboxUri = UriComponentsBuilder.fromUriString(SAFEBOX_ITEMS_URL).build(id);
        mockMvc.perform(get(baseSafeboxUri)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isUnauthorized());
    }

    @Test
    void putSafeboxItemsIntegrationTest() throws Exception {
        String id = "TEST-ID";
        List<String> itemList = Collections.singletonList("ITEM");
        ItemListDto itemListDto = new ItemListDto();
        itemListDto.setItems(itemList);

        URI baseSafeboxUri = UriComponentsBuilder.fromUriString(SAFEBOX_ITEMS_URL).build(id);
        mockMvc.perform(put(baseSafeboxUri).with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(itemListDto))
        ).andExpect(status().isOk());
    }

    @Test
    void putSafeboxItems_noAuthentication401_IntegrationTest() throws Exception {
        String id = "TEST-ID";

        URI baseSafeboxUri = UriComponentsBuilder.fromUriString(SAFEBOX_ITEMS_URL).build(id);
        mockMvc.perform(put(baseSafeboxUri)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isUnauthorized());
    }
}
