package integration.com.rviewer.skeletons.infrastructure.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rviewer.skeletons.SafeboxServiceApplication;
import com.rviewer.skeletons.application.model.CreateSafeboxRequestDto;
import com.rviewer.skeletons.application.model.ItemListDto;
import com.rviewer.skeletons.application.model.SafeboxDto;
import com.rviewer.skeletons.domain.model.Safebox;
import com.rviewer.skeletons.domain.repository.SafeboxRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.util.UriComponentsBuilder;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.net.URI;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = SafeboxServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
class SafeboxApiImplIntegrationTest {

    @Container
    private static RabbitMQContainer rabbitMQContainer = new RabbitMQContainer("rabbitmq:3-management")
            .withQueue("SAFEBOX_CREATED_USER.QUEUE.TEST");

    @DynamicPropertySource
    static void configure(DynamicPropertyRegistry registry) {
        registry.add("spring.rabbitmq.host", rabbitMQContainer::getHost);
        registry.add("spring.rabbitmq.port", rabbitMQContainer::getAmqpPort);
    }

    private static final String SAFEBOX_URL = "/safebox";

    private static final String SAFEBOX_ITEMS_URL = "/safebox/{id}/items";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SafeboxRepository safeboxRepository;

    @Test
    void safeboxPostIntegrationTest() throws Exception {
        String safeboxOwner = "NOT_REGISTERED";

        Optional<Safebox> byOwner = safeboxRepository.findByOwner(safeboxOwner);
        Assertions.assertTrue(byOwner.isEmpty());

        CreateSafeboxRequestDto request = new CreateSafeboxRequestDto().owner(safeboxOwner);
        URI uri = new URI(SAFEBOX_URL);
        MvcResult result = mockMvc.perform(post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isCreated()).andReturn();

        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getResponse());

        SafeboxDto response = objectMapper.readValue(result.getResponse().getContentAsString(), SafeboxDto.class);

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getId());

        byOwner = safeboxRepository.findByOwner(safeboxOwner);
        Assertions.assertTrue(byOwner.isPresent());
        Assertions.assertNotNull(byOwner.get().getItemList());
        Assertions.assertTrue(byOwner.get().getItemList().isEmpty());
    }

    @Test
    void safeboxPost_alreadyExistingException_IntegrationTest() throws Exception {
        String safeboxOwner = "EXISTING_SAFEBOX";

        Optional<Safebox> byOwner = safeboxRepository.findByOwner(safeboxOwner);
        Assertions.assertTrue(byOwner.isPresent());

        CreateSafeboxRequestDto request = new CreateSafeboxRequestDto().owner(safeboxOwner);
        URI uri = new URI(SAFEBOX_URL);
        mockMvc.perform(post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isConflict());
    }

    @Test
    void safeboxIdItemsGetIntegrationTest() throws Exception {
        String id = "EXISTING_SAFEBOX";

        Optional<Safebox> byId = safeboxRepository.findById(id);
        Assertions.assertTrue(byId.isPresent());

        URI uri = UriComponentsBuilder.fromUriString(SAFEBOX_ITEMS_URL).build(id);
        MvcResult result = mockMvc.perform(get(uri))
                .andExpect(status().isOk())
                .andReturn();

        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getResponse());

        ItemListDto response = objectMapper.readValue(result.getResponse().getContentAsString(), ItemListDto.class);

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getItems());

        Assertions.assertEquals(byId.get().getItemList().size(), response.getItems().size());

        byId.get().getItemList().forEach(item -> Assertions.assertTrue(response.getItems().contains(item.getDetail())));
    }

    @Test
    void safeboxIdItemsGet_safeboxDoesNotExist_IntegrationTest() throws Exception {
        String id = UUID.randomUUID().toString();

        Optional<Safebox> byId = safeboxRepository.findById(id);
        Assertions.assertTrue(byId.isEmpty());

        URI uri = UriComponentsBuilder.fromUriString(SAFEBOX_ITEMS_URL).build(id);
        mockMvc.perform(get(uri)).andExpect(status().isNotFound());
    }

    @Test
    void safeboxIdItemsPutIntegrationTest() throws Exception {
        String id = "EMPTY_SAFEBOX";

        Optional<Safebox> byId = safeboxRepository.findById(id);
        Assertions.assertTrue(byId.isPresent());
        Assertions.assertTrue(byId.get().getItemList().isEmpty());

        URI uri = UriComponentsBuilder.fromUriString(SAFEBOX_ITEMS_URL).build(id);
        ItemListDto itemListDto = new ItemListDto().items(Collections.singletonList("TEST"));
        mockMvc.perform(put(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(itemListDto))
        ).andExpect(status().isOk());

        byId = safeboxRepository.findById(id);
        Assertions.assertTrue(byId.isPresent());
        Assertions.assertFalse(byId.get().getItemList().isEmpty());
    }

    @Test
    void safeboxIdItemsPutIntegrationTest_safeboxDoesNotExist_IntegrationTest() throws Exception {
        String id = UUID.randomUUID().toString();

        Optional<Safebox> byId = safeboxRepository.findById(id);
        Assertions.assertTrue(byId.isEmpty());

        URI uri = UriComponentsBuilder.fromUriString(SAFEBOX_ITEMS_URL).build(id);
        ItemListDto itemListDto = new ItemListDto().items(Collections.singletonList("TEST"));
        mockMvc.perform(put(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(itemListDto))
        ).andExpect(status().isNotFound());
    }
}
