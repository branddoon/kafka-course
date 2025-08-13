package com.kafka.course.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafka.course.model.MessageEvent;
import com.kafka.course.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MessageConsumer {

    @Autowired
    MessageService messageService;

    @Autowired
    ObjectMapper objectMapper;

    @KafkaListener(topics = {"${spring.kafka.topic}"})
    public void onMessage(ConsumerRecord<Integer, String> consumerRecord) throws JsonProcessingException {
        log.info("Message received: {}", consumerRecord);
        MessageEvent messageEvent =  objectMapper.readValue(consumerRecord.value(), MessageEvent.class);
        messageService.save(messageEvent);
    }
}
