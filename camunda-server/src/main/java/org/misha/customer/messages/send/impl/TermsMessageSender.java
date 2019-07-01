package org.misha.customer.messages.send.impl;

import lombok.extern.slf4j.Slf4j;
import org.misha.Sender;
import org.misha.customer.dto.Message;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;

import static java.util.concurrent.CompletableFuture.supplyAsync;

/**
 * sends messages to customer-to-contractor topic
 */
@Component
@EnableBinding(Source.class)
@Slf4j
public class TermsMessageSender implements Sender {
    private final MessageChannel output;

    public TermsMessageSender(@Qualifier("output") MessageChannel channel) {
        this.output = channel;
        log.debug("channel: {} is {}", output.getClass(), output);
    }

    @Override
    public MessageChannel output() {
        return output;
    }

    @Override
    public void send(Message<?> m) {
        log.debug("\n\nSender: {};\nmessage: {}", this.getClass().getSimpleName(), m);
        supplyAsync(() -> {
            Sender.super.send(m);
            return null;
        });
    }
}