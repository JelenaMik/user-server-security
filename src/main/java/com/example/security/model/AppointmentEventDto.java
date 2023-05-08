package com.example.security.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
@Builder
public class AppointmentEventDto implements Serializable {
    private Long id;
    private Long userId;
    private Long appointmentId;
    private String details;
}
