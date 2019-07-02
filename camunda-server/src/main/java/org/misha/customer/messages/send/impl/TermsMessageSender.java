package org.misha.customer.messages.send.impl;

import lombok.extern.slf4j.Slf4j;
import org.misha.Sender;
import org.misha.customer.dto.Message;
import org.misha.customer.dto.contractor.SumMessageContent;
import org.misha.customer.dto.customer.TermsMessageContent;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;

import java.util.Observable;
import java.util.Observer;
import java.util.UUID;

import static java.lang.Thread.sleep;
import static java.util.concurrent.CompletableFuture.supplyAsync;

/**
 * sends messages to customer-to-contractor topic
 */
@Component
@EnableBinding(Source.class)
@Slf4j
public class TermsMessageSender implements Sender, Observer {
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

    @Override
    public void update(Observable o, Object arg) {
        SumMessageContent sumMessageContent = (SumMessageContent) arg;
        final Message<TermsMessageContent> msg = new Message<>();
        msg.setMessageType("TermsMessageContent");
        msg.setCorrelationId(UUID.randomUUID().toString());//initial request
        msg.setSender(getClass().getSimpleName());
        msg.setPayload(TermsMessageContent.builder().id(UUID.randomUUID().toString()).left(1).right(sumMessageContent.getSum()).build());
        try {
            sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();Thread.currentThread().interrupt();
        } send(msg);
    }
}