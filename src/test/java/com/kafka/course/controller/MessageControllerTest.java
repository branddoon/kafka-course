package com.kafka.course.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafka.course.model.MessageEvent;
import com.kafka.course.model.MessageEventType;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.KafkaUtils;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EmbeddedKafka(topics = "topic-course-test-1")
@TestPropertySource(properties = {"spring.kafka.producer.bootstrap-servers=${spring.embedded.kafka.brokers}",
        "spring.kafka.admin.properties.bootstrap.servers=${spring.embedded.kafka.brokers}"})
public class MessageControllerTest {

    @Autowired
    TestRestTemplate testRestTemplate;

    @Autowired
    EmbeddedKafkaBroker embeddedKafkaBroker;

    Consumer<Integer, String> consumer;

    @BeforeEach
    void setUp(){
        var config = KafkaTestUtils.consumerProps("message-group-test-1","true", embeddedKafkaBroker);
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        consumer = new DefaultKafkaConsumerFactory<Integer, String>
                (config, new IntegerDeserializer(), new StringDeserializer())
                .createConsumer();
        embeddedKafkaBroker.consumeFromAnEmbeddedTopic(consumer,"topic-course-test-1");
    }

    @AfterEach
    void after(){
        consumer.close();
    }

    @Autowired
    ObjectMapper objectMapper;


    /*
    * Given HttpHeader object and MessageEvent object
    * When send POST request is executed
    * Then return 200 http status
    * */
    @Test
    void test_send_post_method_successful() {
        HttpHeaders httpHeaders = new HttpHeaders();
        var httpEntity = new HttpEntity<>(new MessageEvent(1, new MessageEventType("", "", "")), httpHeaders);
        var r = testRestTemplate.exchange("/api/send", HttpMethod.POST, httpEntity, ResponseEntity.class);
        Assertions.assertEquals(HttpStatus.OK, r.getStatusCode());
    }

}
