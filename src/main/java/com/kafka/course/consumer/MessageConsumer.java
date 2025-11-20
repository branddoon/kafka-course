package com.kafka.course.consumer;

import com.kafka.course.avro.EventMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MessageConsumer {

    @KafkaListener(topics = "${spring.kafka.topic}")
    public void consume(ConsumerRecord<String, EventMessage> consumerRecord) {
        String key = consumerRecord.key();
        EventMessage message = consumerRecord.value();
        log.info("Avro message received for key : " + key + " value : " + message.toString());
    }

}
