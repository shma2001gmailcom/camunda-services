package org.misha.customer.messages.send;

import lombok.extern.slf4j.Slf4j;
import org.misha.SendMessageDelegate;
import org.misha.Sender;
import org.misha.customer.dto.Message;
import org.misha.customer.dto.customer.TermsMessageContent;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * camunda-delegate for sender
 */
@Component
@Slf4j
public class SendTermsMessageDelegate extends SendMessageDelegate<TermsMessageContent> {
    private final Sender messageSender;

    public SendTermsMessageDelegate(@Qualifier("termsMessageSender") Sender sender) {
        super(sender);
        this.messageSender = sender;
    }

    @Override
    public Message<TermsMessageContent> makeMessage() {
        final Message<TermsMessageContent> msg = new Message<>();
        msg.setMessageType("TermsMessageContent");
        msg.setCorrelationId(UUID.randomUUID().toString());//initial request
        msg.setSender(messageSender.getClass().getSimpleName());
        msg.setPayload(TermsMessageContent.builder().id(UUID.randomUUID().toString()).left(1).right(2).build());
        return msg;
    }
}
