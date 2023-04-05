package com.example.security.service;

import com.example.security.responseBodyModel.AppointmentDetailDto;
import com.example.security.responseBodyModel.AppointmentDto;
import com.example.security.responseBodyModel.AppointmentRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AppointmentService {
    List<AppointmentDto> getUserWeekAppointments(Integer week, String role, Long userId);

    AppointmentDto getAppointmentById(Long id);

    AppointmentDetailDto getAppointmentDetailById(Long id);

    AppointmentDto createAppointment(AppointmentRequest appointmentRequest);

    void deleteAppointment(Long id);

    void cancelAppointment(Long id);

    void changeAppointmentStatus(Long id);

    void changeAppointmentType(String appointmentType, Long id);

    void bookAnAppointment(Long clientId, Long appointmentId, String details);
}
