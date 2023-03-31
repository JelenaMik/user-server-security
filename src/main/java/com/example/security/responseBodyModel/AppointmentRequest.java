package com.example.security.responseBodyModel;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentRequest {
    private Long providerId;
    private String startDate;
    private String startHour;
    private String appointmentType;

}
