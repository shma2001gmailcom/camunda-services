package org.misha.customer.messages.listener;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.misha.customer.dto.Message;
import org.misha.customer.dto.contractor.SumMessageContent;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.stereotype.Component;

/**
 * listener for topic contractor-to-customer
 *
 * @see "application.properties"
 */
@Component
@EnableBinding(Sink.class)
@Slf4j
class ContractorsMessageListener implements JavaDelegate {
    @StreamListener(target = Sink.INPUT)
    public void messageReceived(String messageJson) throws Exception {
        log.debug("\n\n---------------\n\nReceiver: {};\njson received: {}", getClass().getSimpleName(), messageJson);
        final TypeReference<Message<JsonNode>> typeRef = new TypeReference<Message<JsonNode>>() {};
        final Message<JsonNode> message = new ObjectMapper().readValue(messageJson, typeRef);
        final SumMessageContent sumContent =
                new ObjectMapper().readValue(message.getPayload().toString(), SumMessageContent.class);
        log.debug("\n\n---------------\n\nReceiver: {};\nmessage: {}", getClass().getSimpleName(), sumContent);
    }

    @Override
    public void execute(final DelegateExecution execution) throws Exception {
        //make it possible to start from ui
    }
}
