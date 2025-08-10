package com.kafka.course.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kafka.course.model.MessageEvent;
import com.kafka.course.producer.MessageProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api")
@RestController
@Slf4j
public class MessageController {

    private final MessageProducer mp;

    public MessageController(MessageProducer mp) {
        this.mp = mp;
    }

    @PostMapping("/send")
    public ResponseEntity<Void> send(@RequestBody MessageEvent messageEvent) throws JsonProcessingException {
        log.info("Message: {}", messageEvent);
        mp.sendMessage(messageEvent);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
