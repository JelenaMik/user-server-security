package com.example.security.responseBodyModel;

import com.example.security.enums.AppointmentStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
@Builder
public class AppointmentDetailDto {

    private Long appointmentDetailId;

    private Long appointmentId;
    private AppointmentStatus status;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime created;
}
