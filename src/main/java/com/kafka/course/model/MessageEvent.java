package com.kafka.course.model;

public record MessageEvent (
        Integer id,
        MessageEventType messageEventType
){}
