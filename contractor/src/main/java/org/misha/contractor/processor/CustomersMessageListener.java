package org.misha.contractor.processor;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.misha.contractor.dto.Message;
import org.misha.contractor.dto.contractor.SumMessageContent;
import org.misha.contractor.dto.customer.TermsMessageContent;
import org.misha.contractor.service.Adder;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Observable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * listens for topic customer-to-contractor to receive data for calculation
 * calls service to obtain a result
 * sends reply into topic contractor-to-customer
 */
@Component
@EnableBinding(Processor.class)
@Slf4j
class CustomersMessageListener extends Observable {
    private final Adder adder;

    CustomersMessageListener(Adder adder) {
        this.adder = adder;
    }

    @StreamListener(target = Processor.INPUT)
    @SendTo(Processor.OUTPUT)
    public Message<SumMessageContent> messageReceived(String messageJson) throws IOException, ExecutionException {
        log.debug("\n\n---------------\n\nProcessor: json received={}", messageJson);
        final TypeReference<Message<TermsMessageContent>> typeRef =
                new TypeReference<Message<TermsMessageContent>>() {};
        final Message<TermsMessageContent> message = new ObjectMapper().readValue(messageJson, typeRef);
        final int left = message.getPayload().getLeft();
        final int right = message.getPayload().getRight();
        log.debug("\n\n---------------\n\nProcessor: {};\nmessageType: {};\nmessage: {};", getClass().getSimpleName(),
                  message);
        final Message<SumMessageContent> sumMessage = makeReplyMessage(message, adder.sum(left, right));
        log.debug("\n\n---------------\n\nProcessor: {};\nmessageType: {};\nmessage: {};\n ready for send.",
                  this.getClass().getSimpleName(), sumMessage.getMessageType(), sumMessage);
        return sumMessage;
    }

    private Message<SumMessageContent> makeReplyMessage(Message<TermsMessageContent> message,
                                                        Future<Integer> calculationPlan
    ) throws ExecutionException {
        final Message<SumMessageContent> msg = new Message<>();
        msg.setMessageType("SumMessageContent");
        msg.setSender(getClass().getSimpleName());
        msg.setCorrelationId(message.getCorrelationId());//populate correlation from incoming message
        final SumMessageContent content = new SumMessageContent();
        final Integer result;
        try {
            result = calculationPlan.get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
        content.setSum(result);
        content.setLeft(message.getPayload().getLeft());
        content.setRight(message.getPayload().getRight());
        msg.setPayload(content);
        return msg;
    }
}

