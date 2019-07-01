package org.misha;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.misha.customer.dto.Message;

import java.util.concurrent.ExecutionException;

@Slf4j
public abstract class SendMessageDelegate<T> implements JavaDelegate {
    private final Sender messageSender;

    protected SendMessageDelegate(final Sender sender) {
        log.debug("\n\n\n{}", this.getClass().getSimpleName());
        this.messageSender = sender;
    }

    @Override
    public void execute(DelegateExecution context) throws Exception {
        final Message<T> msg = makeMessage();
        messageSender.send(msg);
    }

    public abstract Message<T> makeMessage() throws ExecutionException, InterruptedException;
}

