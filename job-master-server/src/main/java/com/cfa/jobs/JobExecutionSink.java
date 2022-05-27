package com.cfa.jobs;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.kafka.support.KafkaSendFailureException;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.ErrorMessage;

/**
 * follow the execution of a job
 */
@EnableBinding(Sink.class)
@Slf4j
public class JobExecutionSink {

    /**
     * to intercept unsent messages
     */
    @ServiceActivator(inputChannel = "errorChannel")
    public void errorChannel(final ErrorMessage errorMessage) {
        final Message<?> locFailedMessage = ((KafkaSendFailureException) errorMessage.getPayload()).getFailedMessage();
        assert locFailedMessage != null;
        final MessageHeaders locHeaders = locFailedMessage.getHeaders();
        log.info("errorChannel, MESSAGE NOT SEND : {} ", locHeaders);
    }

    @SneakyThrows
    @StreamListener(target = Sink.INPUT, condition = "headers['custom_info']=='end'")
    public void receiveResult(final Message<String> message) {
        log.info("[Server END] received result {} ", message.getPayload());
    }

}