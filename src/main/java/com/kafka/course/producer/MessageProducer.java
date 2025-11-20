package com.kafka.course.producer;

import com.kafka.course.avro.EventMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.kafka.support.SendResult;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Component
@Slf4j
public class MessageProducer {

    private final KafkaTemplate<String, EventMessage> kt;

    @Value("${spring.kafka.topic}")
    public String topic;

    public MessageProducer(KafkaTemplate<String, EventMessage> kt) {
        this.kt = kt;
    }

    public void send(EventMessage eventMessage){
        CompletableFuture<SendResult<String, EventMessage>> future =
                kt.send(topic, UUID.randomUUID().toString(), eventMessage);
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                System.out.println("Sent message=[" + eventMessage +
                        "] with offset=[" + result.getRecordMetadata().offset() + "]");
            } else {
                System.out.println("Unable to send message=[" +
                        eventMessage + "] due to : " + ex.getMessage());
            }
        });
    }
}
