package com.kafka.course.jpa;

import com.kafka.course.entity.Message;
import org.springframework.data.repository.CrudRepository;

public interface MessageEventRepository extends CrudRepository<Message, Integer> {
}
