package com.kafka.course.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafka.course.model.MessageEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.kafka.support.SendResult;

import java.util.List;

@Component
@Slf4j
public class MessageProducer {

    private final KafkaTemplate<Integer, String> kt;

    private final ObjectMapper om;

    @Value("${spring.kafka.topic}")
    public String topic;

    public MessageProducer(KafkaTemplate<Integer, String> kt, ObjectMapper om) {
        this.kt = kt;
        this.om = om;
    }

    public void sendMessage(MessageEvent messageEvent) throws JsonProcessingException{

        var key = messageEvent.id();

        var value = om.writeValueAsString(messageEvent);

        var cf = kt.send(buildProducerRecord(key, value));

        cf.whenComplete((result, err) -> {
            if (err != null) {
                handleFailure(key, value, err);
            } else {
                handleSuccess(key, value, result);
            }
        });
    }

    private ProducerRecord<Integer, String> buildProducerRecord(Integer key, String value){
        List<Header> headers = List.of(new RecordHeader("businessCode", "HBC" .getBytes()));
        return new ProducerRecord<>(topic, 1, key, value, headers);
    }

    private void handleSuccess(Integer key, String value, SendResult<Integer, String> sendResult){
        log.info("Message Sent Successfully for the key : {} and the value : {} , partition is {} ",
                key, value, sendResult.getRecordMetadata().partition());
    }
    private void handleFailure(Integer key, String value, Throwable ex) {
        log.error("Error sending the message with key: {} and value: {} and the exception is {} ",key, value, ex.getMessage(), ex);
    }
}
