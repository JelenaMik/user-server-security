package com.example.security.web;

import com.example.security.kafka.MessageRequest;
import com.example.security.model.AppointmentEventDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("api/v1/events")
@RestController
@Slf4j
public class KafkaController {
    private KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaController(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostMapping
    public void publish(@RequestBody AppointmentEventDto eventDto){
        log.info("EventDto {}", eventDto);
        kafkaTemplate.send("response", eventDto);

    }
}
