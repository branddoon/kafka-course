package com.kafka.course.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafka.course.model.MessageEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.kafka.support.SendResult;
import java.util.concurrent.CompletableFuture;

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

    public CompletableFuture<SendResult<Integer, String>> sendMessage(MessageEvent messageEvent) throws JsonProcessingException{

        var key = messageEvent.id();

        var value = om.writeValueAsString(messageEvent);

        var cf = kt.send(topic, key, value);

        return cf.whenComplete((result, err) -> {
           if(err == null){
                handleFailure(key, value, err);
           }else{
               handleSuccess(key, value, result);
           }
        });
    }

    private void handleSuccess(Integer key, String value, SendResult<Integer, String> sendResult){
        log.info("Message Sent Successfully for the key : {} and the value : {} , partition is {} ",
                key, value, sendResult);
    }
    private void handleFailure(Integer key, String value, Throwable ex) {
        log.error("Error sending the message and the exception is {} ", ex.getMessage(), ex);
    }
}
