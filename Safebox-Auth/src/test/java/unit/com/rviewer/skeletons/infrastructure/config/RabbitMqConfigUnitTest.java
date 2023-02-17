package unit.com.rviewer.skeletons.infrastructure.config;

import com.rviewer.skeletons.infrastructure.config.RabbitMqConfig;
import com.rviewer.skeletons.infrastructure.config.SafeboxServiceMessagingConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;

@ExtendWith(MockitoExtension.class)
class RabbitMqConfigUnitTest {

    @InjectMocks
    private RabbitMqConfig rabbitMqConfig;

    @Mock
    private SafeboxServiceMessagingConfig safeboxServiceMessagingConfig;

    @Test
    void queueUnitTest() {
        Mockito.when(safeboxServiceMessagingConfig.getQueue()).thenReturn("TEST");

        Queue queue = rabbitMqConfig.queue();

        Mockito.verify(safeboxServiceMessagingConfig).getQueue();

        Assertions.assertNotNull(queue);
    }

    @Test
    void exchangeUnitTest() {
        DirectExchange exchange = rabbitMqConfig.exchange();

        Mockito.verify(safeboxServiceMessagingConfig).getExchange();

        Assertions.assertNotNull(exchange);
    }

    @Test
    void bingingUnitTest() {
        Queue queue = Mockito.mock(Queue.class);
        DirectExchange exchange = Mockito.mock(DirectExchange.class);

        Binding binding = rabbitMqConfig.binding(queue, exchange);

        Mockito.verify(safeboxServiceMessagingConfig).getRoutingKey();

        Assertions.assertNotNull(binding);
    }

    @Test
    void jsonMessageConverterUnitTest() {
        Assertions.assertNotNull(rabbitMqConfig.jsonMessageConverter());
    }
}