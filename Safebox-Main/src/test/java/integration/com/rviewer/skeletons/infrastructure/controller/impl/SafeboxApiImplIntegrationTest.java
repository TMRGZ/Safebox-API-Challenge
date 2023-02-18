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
import com.rviewer.skeletons.infrastructure.rest.safebox.holder.model.HolderItemListDto;
import com.rviewer.skeletons.infrastructure.rest.safebox.holder.model.HolderSafeboxDto;
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
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@AutoConfigureWireMock(port = 0)
@SpringBootTest(classes = SafeboxMainApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SafeboxApiImplIntegrationTest {

    private static final String SAFEBOX_BASE_URL = "/safebox";
    private static final String SPECIFIC_SAFEBOX_URL = SAFEBOX_BASE_URL + "/{id}";
    private static final String OPEN_SAFEBOX_URL = SAFEBOX_BASE_URL + "/{id}/open";
    private static final String SAFEBOX_ITEMS_URL = SAFEBOX_BASE_URL + "/{id}/items";
    private static final String SAFEBOX_AUTH_POST_USER_URL = "/user";
    private static final String SAFEBOX_AUTH_LOGIN_URL = "/login";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

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
    void postSafebox_authSeverBadRequest_IntegrationTest() throws Exception {
        CreateSafeboxRequestDto createSafeboxRequestDto = new CreateSafeboxRequestDto();
        createSafeboxRequestDto.setName("TEST-USER");
        createSafeboxRequestDto.setPassword("TEST-PASSWORD");

        URI safeboxAuthUserUri = URI.create(SAFEBOX_AUTH_POST_USER_URL);
        WireMock.stubFor(WireMock.post(WireMock.urlEqualTo(safeboxAuthUserUri.getPath()))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withStatus(HttpStatus.BAD_REQUEST.value())
                )
        );

        URI baseSafeboxUri = URI.create(SAFEBOX_BASE_URL);
        mockMvc.perform(post(baseSafeboxUri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createSafeboxRequestDto))
        ).andExpect(status().isInternalServerError());
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

        HolderSafeboxDto safeboxDto = new HolderSafeboxDto();
        safeboxDto.setId(id);

        URI safeboxAuthLoginUri = URI.create(SAFEBOX_AUTH_LOGIN_URL);
        WireMock.stubFor(WireMock.post(WireMock.urlEqualTo(safeboxAuthLoginUri.getPath()))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(objectMapper.writeValueAsString(loginResponseDto))
                )
        );

        URI safeboxHolderGetUri = UriComponentsBuilder.fromUriString(SPECIFIC_SAFEBOX_URL).build(id);
        WireMock.stubFor(WireMock.get(WireMock.urlEqualTo(safeboxHolderGetUri.getPath()))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(objectMapper.writeValueAsString(safeboxDto))
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
    void openSafebox_noAuthentication_IntegrationTest() throws Exception {
        String id = "TEST-ID";

        URI openSafeboxUri = UriComponentsBuilder.fromUriString(OPEN_SAFEBOX_URL).build(id);
        mockMvc.perform(get(openSafeboxUri)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isUnauthorized());
    }

    @Test
    void openSafebox_safeboxLocked_IntegrationTest() throws Exception {
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
    void openSafebox_safeboxUserDoesNotExist_IntegrationTest() throws Exception {
        String id = "TEST-ID";
        String username = "TEST-USER";
        String password = "TEST-PASSWORD";

        URI safeboxAuthLoginUri = URI.create(SAFEBOX_AUTH_LOGIN_URL);
        WireMock.stubFor(WireMock.post(WireMock.urlEqualTo(safeboxAuthLoginUri.getPath()))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withStatus(HttpStatus.NOT_FOUND.value())
                )
        );

        URI openSafeboxUri = UriComponentsBuilder.fromUriString(OPEN_SAFEBOX_URL).build(id);
        mockMvc.perform(get(openSafeboxUri).with(httpBasic(username, password))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isForbidden());
    }

    @Test
    void openSafebox_safeboxUserUnauthorized_IntegrationTest() throws Exception {
        String id = "TEST-ID";
        String username = "TEST-USER";
        String password = "TEST-PASSWORD";

        URI safeboxAuthLoginUri = URI.create(SAFEBOX_AUTH_LOGIN_URL);
        WireMock.stubFor(WireMock.post(WireMock.urlEqualTo(safeboxAuthLoginUri.getPath()))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withStatus(HttpStatus.UNAUTHORIZED.value())
                )
        );

        URI openSafeboxUri = UriComponentsBuilder.fromUriString(OPEN_SAFEBOX_URL).build(id);
        mockMvc.perform(get(openSafeboxUri).with(httpBasic(username, password))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isForbidden());
    }

    @Test
    void openSafebox_safeboxUserForbidden_IntegrationTest() throws Exception {
        String id = "TEST-ID";
        String username = "TEST-USER";
        String password = "TEST-PASSWORD";

        URI safeboxAuthLoginUri = URI.create(SAFEBOX_AUTH_LOGIN_URL);
        WireMock.stubFor(WireMock.post(WireMock.urlEqualTo(safeboxAuthLoginUri.getPath()))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withStatus(HttpStatus.FORBIDDEN.value())
                )
        );

        URI openSafeboxUri = UriComponentsBuilder.fromUriString(OPEN_SAFEBOX_URL).build(id);
        mockMvc.perform(get(openSafeboxUri).with(httpBasic(username, password))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isForbidden());
    }

    @Test
    void openSafebox_safeboxAuthBadRequest_IntegrationTest() throws Exception {
        String id = "TEST-ID";
        String username = "TEST-USER";
        String password = "TEST-PASSWORD";

        URI safeboxAuthLoginUri = URI.create(SAFEBOX_AUTH_LOGIN_URL);
        WireMock.stubFor(WireMock.post(WireMock.urlEqualTo(safeboxAuthLoginUri.getPath()))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withStatus(HttpStatus.BAD_REQUEST.value())
                )
        );

        URI openSafeboxUri = UriComponentsBuilder.fromUriString(OPEN_SAFEBOX_URL).build(id);
        mockMvc.perform(get(openSafeboxUri).with(httpBasic(username, password))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isForbidden());
    }

    @Test
    void openSafebox_authServerFailed_IntegrationTest() throws Exception {
        String id = "TEST-ID";
        String username = "TEST-USER";
        String password = "TEST-PASSWORD";

        URI safeboxAuthLoginUri = URI.create(SAFEBOX_AUTH_LOGIN_URL);
        WireMock.stubFor(WireMock.post(WireMock.urlEqualTo(safeboxAuthLoginUri.getPath()))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                )
        );

        URI openSafeboxUri = UriComponentsBuilder.fromUriString(OPEN_SAFEBOX_URL).build(id);
        mockMvc.perform(get(openSafeboxUri).with(httpBasic(username, password))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isForbidden());
    }

    @Test
    void openSafebox_safeboxDoesNotExist_IntegrationTest() throws Exception {
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

        URI safeboxHolderGetUri = UriComponentsBuilder.fromUriString(SPECIFIC_SAFEBOX_URL).build(id);
        WireMock.stubFor(WireMock.get(WireMock.urlEqualTo(safeboxHolderGetUri.getPath()))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withStatus(HttpStatus.NOT_FOUND.value())
                )
        );

        URI openSafeboxUri = UriComponentsBuilder.fromUriString(OPEN_SAFEBOX_URL).build(id);
        mockMvc.perform(get(openSafeboxUri).with(httpBasic(username, password))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound());
    }

    @Test
    void openSafebox_safeboxHolderFailed_IntegrationTest() throws Exception {
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

        URI safeboxHolderGetUri = UriComponentsBuilder.fromUriString(SPECIFIC_SAFEBOX_URL).build(id);
        WireMock.stubFor(WireMock.get(WireMock.urlEqualTo(safeboxHolderGetUri.getPath()))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                )
        );

        URI openSafeboxUri = UriComponentsBuilder.fromUriString(OPEN_SAFEBOX_URL).build(id);
        mockMvc.perform(get(openSafeboxUri).with(httpBasic(username, password))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadGateway());
    }

    @Test
    void openSafebox_safeboxHolderBadRequest_IntegrationTest() throws Exception {
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

        URI safeboxHolderGetUri = UriComponentsBuilder.fromUriString(SPECIFIC_SAFEBOX_URL).build(id);
        WireMock.stubFor(WireMock.get(WireMock.urlEqualTo(safeboxHolderGetUri.getPath()))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withStatus(HttpStatus.BAD_REQUEST.value())
                )
        );

        URI openSafeboxUri = UriComponentsBuilder.fromUriString(OPEN_SAFEBOX_URL).build(id);
        mockMvc.perform(get(openSafeboxUri).with(httpBasic(username, password))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isInternalServerError());
    }

    @Test
    void getSafeboxItemsIntegrationTest() throws Exception {
        String id = "TEST-ID";

        HolderItemListDto itemListDto = new HolderItemListDto();
        itemListDto.setItems(Collections.singletonList("ITEM"));

        URI safeboxHolderGetUri = UriComponentsBuilder.fromUriString(SAFEBOX_ITEMS_URL).build(id);
        WireMock.stubFor(WireMock.get(WireMock.urlEqualTo(safeboxHolderGetUri.getPath()))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(objectMapper.writeValueAsString(itemListDto))
                )
        );

        URI baseSafeboxUri = UriComponentsBuilder.fromUriString(SAFEBOX_ITEMS_URL).build(id);
        MvcResult result = mockMvc.perform(get(baseSafeboxUri).with(opaqueToken())
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
    void getSafeboxItems_safeboxDoesNotExist_IntegrationTest() throws Exception {
        String id = "TEST-ID";

        HolderItemListDto itemListDto = new HolderItemListDto();
        itemListDto.setItems(Collections.singletonList("ITEM"));

        URI safeboxHolderGetUri = UriComponentsBuilder.fromUriString(SAFEBOX_ITEMS_URL).build(id);
        WireMock.stubFor(WireMock.get(WireMock.urlEqualTo(safeboxHolderGetUri.getPath()))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withStatus(HttpStatus.NOT_FOUND.value())
                )
        );

        URI baseSafeboxUri = UriComponentsBuilder.fromUriString(SAFEBOX_ITEMS_URL).build(id);
        mockMvc.perform(get(baseSafeboxUri).with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound());
    }

    @Test
    void getSafeboxItems_safeboxHolderFailed_IntegrationTest() throws Exception {
        String id = "TEST-ID";

        HolderItemListDto itemListDto = new HolderItemListDto();
        itemListDto.setItems(Collections.singletonList("ITEM"));

        URI safeboxHolderGetUri = UriComponentsBuilder.fromUriString(SAFEBOX_ITEMS_URL).build(id);
        WireMock.stubFor(WireMock.get(WireMock.urlEqualTo(safeboxHolderGetUri.getPath()))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                )
        );

        URI baseSafeboxUri = UriComponentsBuilder.fromUriString(SAFEBOX_ITEMS_URL).build(id);
        mockMvc.perform(get(baseSafeboxUri).with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadGateway());
    }

    @Test
    void getSafeboxItems_safeboxHolderBadRequest_IntegrationTest() throws Exception {
        String id = "TEST-ID";

        HolderItemListDto itemListDto = new HolderItemListDto();
        itemListDto.setItems(Collections.singletonList("ITEM"));

        URI safeboxHolderGetUri = UriComponentsBuilder.fromUriString(SAFEBOX_ITEMS_URL).build(id);
        WireMock.stubFor(WireMock.get(WireMock.urlEqualTo(safeboxHolderGetUri.getPath()))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withStatus(HttpStatus.BAD_REQUEST.value())
                )
        );

        URI baseSafeboxUri = UriComponentsBuilder.fromUriString(SAFEBOX_ITEMS_URL).build(id);
        mockMvc.perform(get(baseSafeboxUri).with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isInternalServerError());
    }

    @Test
    void putSafeboxItemsIntegrationTest() throws Exception {
        String id = "TEST-ID";
        List<String> itemList = Collections.singletonList("ITEM");
        ItemListDto itemListDto = new ItemListDto();
        itemListDto.setItems(itemList);

        URI safeboxHolderPutUri = UriComponentsBuilder.fromUriString(SAFEBOX_ITEMS_URL).build(id);
        WireMock.stubFor(WireMock.put(WireMock.urlEqualTo(safeboxHolderPutUri.getPath()))
                .withRequestBody(equalToJson(objectMapper.writeValueAsString(itemListDto)))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                )
        );

        URI baseSafeboxUri = UriComponentsBuilder.fromUriString(SAFEBOX_ITEMS_URL).build(id);
        mockMvc.perform(put(baseSafeboxUri).with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(itemListDto))
        ).andExpect(status().isCreated());
    }

    @Test
    void putSafeboxItems_noAuthentication401_IntegrationTest() throws Exception {
        String id = "TEST-ID";

        URI baseSafeboxUri = UriComponentsBuilder.fromUriString(SAFEBOX_ITEMS_URL).build(id);
        mockMvc.perform(put(baseSafeboxUri)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isUnauthorized());
    }

    @Test
    void putSafeboxItems_safeboxDoesNotExist_IntegrationTest() throws Exception {
        String id = "TEST-ID";
        List<String> itemList = Collections.singletonList("ITEM");
        ItemListDto itemListDto = new ItemListDto();
        itemListDto.setItems(itemList);

        URI safeboxHolderPutUri = UriComponentsBuilder.fromUriString(SAFEBOX_ITEMS_URL).build(id);
        WireMock.stubFor(WireMock.put(WireMock.urlEqualTo(safeboxHolderPutUri.getPath()))
                .withRequestBody(equalToJson(objectMapper.writeValueAsString(itemListDto)))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withStatus(HttpStatus.NOT_FOUND.value())
                )
        );

        URI baseSafeboxUri = UriComponentsBuilder.fromUriString(SAFEBOX_ITEMS_URL).build(id);
        mockMvc.perform(put(baseSafeboxUri).with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(itemListDto))
        ).andExpect(status().isNotFound());
    }

    @Test
    void putSafeboxItems_safeboxHolderFailed_IntegrationTest() throws Exception {
        String id = "TEST-ID";
        List<String> itemList = Collections.singletonList("ITEM");
        ItemListDto itemListDto = new ItemListDto();
        itemListDto.setItems(itemList);

        URI safeboxHolderPutUri = UriComponentsBuilder.fromUriString(SAFEBOX_ITEMS_URL).build(id);
        WireMock.stubFor(WireMock.put(WireMock.urlEqualTo(safeboxHolderPutUri.getPath()))
                .withRequestBody(equalToJson(objectMapper.writeValueAsString(itemListDto)))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                )
        );

        URI baseSafeboxUri = UriComponentsBuilder.fromUriString(SAFEBOX_ITEMS_URL).build(id);
        mockMvc.perform(put(baseSafeboxUri).with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(itemListDto))
        ).andExpect(status().isBadGateway());
    }

    @Test
    void putSafeboxItems_safeboxHolderBadRequest_IntegrationTest() throws Exception {
        String id = "TEST-ID";
        List<String> itemList = Collections.singletonList("ITEM");
        ItemListDto itemListDto = new ItemListDto();
        itemListDto.setItems(itemList);

        URI safeboxHolderPutUri = UriComponentsBuilder.fromUriString(SAFEBOX_ITEMS_URL).build(id);
        WireMock.stubFor(WireMock.put(WireMock.urlEqualTo(safeboxHolderPutUri.getPath()))
                .withRequestBody(equalToJson(objectMapper.writeValueAsString(itemListDto)))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withStatus(HttpStatus.BAD_REQUEST.value())
                )
        );

        URI baseSafeboxUri = UriComponentsBuilder.fromUriString(SAFEBOX_ITEMS_URL).build(id);
        mockMvc.perform(put(baseSafeboxUri).with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(itemListDto))
        ).andExpect(status().isInternalServerError());
    }
}
