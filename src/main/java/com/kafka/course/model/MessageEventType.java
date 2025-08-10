package com.kafka.course.model;

public record MessageEventType(
        String message,
        String addressee,
        String sender
){}
