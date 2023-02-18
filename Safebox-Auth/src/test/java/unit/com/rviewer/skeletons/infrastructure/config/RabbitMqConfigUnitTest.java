package unit.com.rviewer.skeletons.infrastructure.config;

import com.rviewer.skeletons.infrastructure.config.RabbitMqConfig;
import com.rviewer.skeletons.infrastructure.config.SafeboxHolderMessagingConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;

@ExtendWith(MockitoExtension.class)
class RabbitMqConfigUnitTest {

    @InjectMocks
    private RabbitMqConfig rabbitMqConfig;

    @Mock
    private SafeboxHolderMessagingConfig safeboxHolderMessagingConfig;

    @Test
    void queueUnitTest() {
        Mockito.when(safeboxHolderMessagingConfig.getQueue()).thenReturn("TEST");

        Queue queue = rabbitMqConfig.queue();

        Mockito.verify(safeboxHolderMessagingConfig).getQueue();

        Assertions.assertNotNull(queue);
    }

    @Test
    void exchangeUnitTest() {
        DirectExchange exchange = rabbitMqConfig.exchange();

        Mockito.verify(safeboxHolderMessagingConfig).getExchange();

        Assertions.assertNotNull(exchange);
    }

    @Test
    void bingingUnitTest() {
        Queue queue = Mockito.mock(Queue.class);
        DirectExchange exchange = Mockito.mock(DirectExchange.class);

        Binding binding = rabbitMqConfig.binding(queue, exchange);

        Mockito.verify(safeboxHolderMessagingConfig).getRoutingKey();

        Assertions.assertNotNull(binding);
    }

    @Test
    void jsonMessageConverterUnitTest() {
        Assertions.assertNotNull(rabbitMqConfig.jsonMessageConverter());
    }
}
