package com.example.security.web;

import org.springframework.kafka.core.KafkaTemplate;
import com.example.security.kafka.MessageRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/messages")
public class KafkaTestController {
    private KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaTestController(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }


    @PostMapping
    public void publish(@RequestBody MessageRequest request){
        kafkaTemplate.send("test", request.message());
    }

}
