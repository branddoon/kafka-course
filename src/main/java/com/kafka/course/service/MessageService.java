package com.kafka.course.service;

import com.kafka.course.entity.Message;
import com.kafka.course.jpa.MessageEventRepository;
import com.kafka.course.model.MessageEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MessageService {


    @Autowired
    MessageEventRepository repository;

    public void save(MessageEvent messageEvent){
        log.info("Starting save message in H2...");
        Message message = Message.builder()
                .id(messageEvent.id())
                .addressee(messageEvent.messageEventType().addressee())
                .sender(messageEvent.messageEventType().sender())
                .message(messageEvent.messageEventType().message())
                .build();
        repository.save(message);
        log.info("Finished save message in H2");
    }
}
