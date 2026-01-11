package com.kafka.course.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafka.course.model.MessageEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Component
@Slf4j
public class MessageProducer {

    @Autowired
    private KafkaTemplate<Integer, String> kt;

    @Autowired
    private ObjectMapper om;

    @Value("${spring.kafka.topic}")
    public String topic;

    public void sendMessage(MessageEvent messageEvent) throws JsonProcessingException, ExecutionException, InterruptedException, TimeoutException {
        var key = messageEvent.id();
        var value = om.writeValueAsString(messageEvent);
        kt.send(topic, key, value)
                .orTimeout(3, TimeUnit.SECONDS)
                .whenComplete((res, ex) -> {
                    if (ex != null) {
                        log.info("Error: {}", ex.getMessage());
                    } else {
                        log.info("Completed: {}", res.getRecordMetadata());
                    }
                });

    }
}
