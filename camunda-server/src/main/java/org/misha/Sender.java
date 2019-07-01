package org.misha;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.misha.customer.dto.Message;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;

public interface Sender {
    default void send(Message<?> m) {
        String jsonMessage;
        try {
            jsonMessage = new ObjectMapper().writeValueAsString(m);
            output().send(MessageBuilder.withPayload(jsonMessage).setHeader("messageType", m.getMessageType()).build());
        } catch (Exception e) {
            throw new RuntimeException("Could not transform and send message due to: " + e.getMessage(), e);
        }
    }

    MessageChannel output();
}
